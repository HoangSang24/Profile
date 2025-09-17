using g1_hangmanhero.Data;
using g1_hangmanhero.Models;
using System.Collections.ObjectModel;
using System.Linq;
using System.Windows;
using System.Windows.Input;

namespace g1_hangmanhero.ViewModels
{
    public class GameSetupAdminViewModel : ViewModelBase
    {
        private readonly Player _adminUser;

        public string CurrentUserName
        {
            get => _adminUser.Username;
        }

        #region Word Management
        public ObservableCollection<Word> Words { get; set; }

        private Word _selectedWord;
        public Word SelectedWord
        {
            get => _selectedWord;
            set
            {
                _selectedWord = value;
                OnPropertyChanged();

                if (_selectedWord != null)
                {
                    NewWordText = _selectedWord.Text;
                    NewWordCategory = _selectedWord.Category;
                    NewWordDifficulty = _selectedWord.Difficulty;
                }
            }
        }

        private string _newWordText;
        public string NewWordText
        {
            get => _newWordText;
            set { _newWordText = value; OnPropertyChanged(); }
        }

        private string _newWordCategory;
        public string NewWordCategory
        {
            get => _newWordCategory;
            set { _newWordCategory = value; OnPropertyChanged(); }
        }

        private string _newWordDifficulty;
        public string NewWordDifficulty
        {
            get => _newWordDifficulty;
            set { _newWordDifficulty = value; OnPropertyChanged(); }
        }

        public ObservableCollection<string> AvailableDifficulties { get; set; }

        public ICommand AddWordCommand { get; }
        public ICommand UpdateWordCommand { get; }
        public ICommand DeleteWordCommand { get; }
        #endregion

        #region Player Management
        public ObservableCollection<Player> Players { get; set; }

        private Player _selectedPlayer;
        public Player SelectedPlayer
        {
            get => _selectedPlayer;
            set
            {
                _selectedPlayer = value;
                OnPropertyChanged();

                if (_selectedPlayer != null)
                {
                    NewUsername = _selectedPlayer.Username;
                    NewDefaultDifficulty = _selectedPlayer.DefaultDifficulty;
                    NewRole = _selectedPlayer.Role.ToString();
                }
            }
        }

        private string _newUsername;
        public string NewUsername
        {
            get => _newUsername;
            set { _newUsername = value; OnPropertyChanged(); }
        }

        private string _newDefaultDifficulty;
        public string NewDefaultDifficulty
        {
            get => _newDefaultDifficulty;
            set { _newDefaultDifficulty = value; OnPropertyChanged(); }
        }

        private string _newRole;
        public string NewRole
        {
            get => _newRole;
            set { _newRole = value; OnPropertyChanged(); }
        }

        public ObservableCollection<string> AvailableRoles { get; set; }

        public ICommand UpdatePlayerCommand { get; }
        public ICommand DeletePlayerCommand { get; }
        #endregion

        public ICommand LogoutCommand { get; }

        private string _selectedTab;
        public string SelectedTab
        {
            get => _selectedTab;
            set { _selectedTab = value; OnPropertyChanged(); }
        }

        public GameSetupAdminViewModel(Player adminUser)
        {
            _adminUser = adminUser;

            // Word Commands
            AddWordCommand = new RelayCommand(ExecuteAddWord, CanExecuteAddWord);
            UpdateWordCommand = new RelayCommand(ExecuteUpdateWord, CanExecuteUpdateOrDeleteWord);
            DeleteWordCommand = new RelayCommand(ExecuteDeleteWord, CanExecuteUpdateOrDeleteWord);

            // Player Commands
            UpdatePlayerCommand = new RelayCommand(ExecuteUpdatePlayer, CanExecuteUpdateOrDeletePlayer);
            DeletePlayerCommand = new RelayCommand(ExecuteDeletePlayer, CanExecuteUpdateOrDeletePlayer);

            LogoutCommand = new RelayCommand(ExecuteLogout);

            LoadAvailableDifficulties();
            LoadAvailableRoles();
            LoadWords();
            LoadPlayers();

            SelectedTab = "Words"; // Default to Words tab
        }

        public event Action RequestLogout;
        private void ExecuteLogout(object parameter)
        {
            RequestLogout?.Invoke();
        }

        #region Word Management Methods
        private bool IsValidWord()
        {
            // Ensure that the word is not empty or whitespace, contains only letters, and has at least 4 characters
            return !string.IsNullOrWhiteSpace(NewWordText) &&
                   NewWordText.Length >= 4 &&               // Check that the word is at least 4 characters
                   NewWordText.All(c => Char.IsLetter(c)) && // Ensure that the word contains only letters
                   !string.IsNullOrWhiteSpace(NewWordCategory) &&
                   !string.IsNullOrWhiteSpace(NewWordDifficulty);
        }

        private bool CanExecuteAddWord(object parameter)
        {
            return IsValidWord();
        }

        private bool CanExecuteUpdateOrDeleteWord(object parameter)
        {
            return SelectedWord != null;
        }

        private void LoadAvailableDifficulties()
        {
            try
            {
                using (var context = new HangmanHeroContext())
                {
                    var difficultiesList = context.Words
                                                  .Select(w => w.Difficulty)
                                                  .Distinct()
                                                  .ToList();
                    AvailableDifficulties = new ObservableCollection<string>(difficultiesList);
                }
            }
            catch (System.Exception ex)
            {
                MessageBox.Show($"Could not load the list of difficulties: {ex.Message}", "Database Error", MessageBoxButton.OK, MessageBoxImage.Error);
                AvailableDifficulties = new ObservableCollection<string>();
            }
        }

        private void LoadWords()
        {
            try
            {
                using (var context = new HangmanHeroContext())
                {
                    Words = new ObservableCollection<Word>(context.Words.ToList());
                }
            }
            catch (System.Exception ex)
            {
                MessageBox.Show($"Error loading words: {ex.Message}", "Database Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        private void ExecuteAddWord(object parameter)
        {
            if (!IsValidWord())
            {
                MessageBox.Show("Please enter valid input (Text must be >= 4)", "Validation Error", MessageBoxButton.OK, MessageBoxImage.Warning);
                return;
            }

            var newWord = new Word
            {
                Text = NewWordText.Trim(),
                Category = NewWordCategory.Trim(),
                Difficulty = NewWordDifficulty
            };

            try
            {
                using (var context = new HangmanHeroContext())
                {
                    context.Words.Add(newWord);
                    context.SaveChanges();
                    Words.Add(newWord);
                }

                NewWordText = string.Empty;
                NewWordCategory = string.Empty;
                NewWordDifficulty = null;

                MessageBox.Show("New word added successfully!", "Success", MessageBoxButton.OK, MessageBoxImage.Information);
            }
            catch (System.Exception ex)
            {
                MessageBox.Show($"Error adding word: {ex.Message}", "Database Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        private void ExecuteUpdateWord(object parameter)
        {
            if (SelectedWord == null) return;
            if (!IsValidWord())
            {
                MessageBox.Show("Please enter valid input (Text must be >= 4).", "Validation Error", MessageBoxButton.OK, MessageBoxImage.Warning);
                return;
            }
            try
            {
                using (var context = new HangmanHeroContext())
                {
                    var wordToUpdate = context.Words.Find(SelectedWord.WordId);
                    if (wordToUpdate != null)
                    {
                        wordToUpdate.Text = NewWordText.Trim();
                        wordToUpdate.Category = NewWordCategory.Trim();
                        wordToUpdate.Difficulty = NewWordDifficulty;
                        context.SaveChanges();

                        var index = Words.IndexOf(SelectedWord);
                        if (index >= 0)
                        {
                            Words[index] = wordToUpdate;
                        }

                        MessageBox.Show("Word updated successfully!", "Success", MessageBoxButton.OK, MessageBoxImage.Information);
                    }
                }
            }
            catch (System.Exception ex)
            {
                MessageBox.Show($"Error updating word: {ex.Message}", "Database Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        private void ExecuteDeleteWord(object parameter)
        {
            if (SelectedWord == null) return;

            var result = MessageBox.Show($"Are you sure you want to delete the word '{SelectedWord.Text}'?", "Confirm Deletion", MessageBoxButton.YesNo, MessageBoxImage.Question);
            if (result == MessageBoxResult.No) return;

            try
            {
                using (var context = new HangmanHeroContext())
                {
                    var wordToDelete = context.Words.Find(SelectedWord.WordId);
                    if (wordToDelete != null)
                    {
                        context.Words.Remove(wordToDelete);
                        context.SaveChanges();
                        Words.Remove(SelectedWord);
                    }
                }
            }
            catch (System.Exception ex)
            {
                MessageBox.Show($"Error deleting word: {ex.Message}", "Database Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }
        #endregion

        #region Player Management Methods
        private bool IsValidPlayer()
        {
            return !string.IsNullOrWhiteSpace(NewUsername) &&
                   !string.IsNullOrWhiteSpace(NewDefaultDifficulty) &&
                   !string.IsNullOrWhiteSpace(NewRole) &&
                   int.TryParse(NewRole, out _);
        }

        private bool CanExecuteUpdateOrDeletePlayer(object parameter)
        {
            return SelectedPlayer != null;
        }

        private void LoadAvailableRoles()
        {
            AvailableRoles = new ObservableCollection<string> { "0", "1" }; // 0 for regular user, 1 for admin
        }

        private void LoadPlayers()
        {
            try
            {
                using (var context = new HangmanHeroContext())
                {
                    Players = new ObservableCollection<Player>(context.Players.ToList());
                }
            }
            catch (System.Exception ex)
            {
                MessageBox.Show($"Error loading players: {ex.Message}", "Database Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        private void ExecuteUpdatePlayer(object parameter)
        {
            if (SelectedPlayer == null) return;
            if (!IsValidPlayer())
            {
                MessageBox.Show("Please fill in all the required fields with valid data.", "Validation Error", MessageBoxButton.OK, MessageBoxImage.Warning);
                return;
            }

            try
            {
                using (var context = new HangmanHeroContext())
                {
                    var playerToUpdate = context.Players.Find(SelectedPlayer.PlayerId);
                    if (playerToUpdate != null)
                    {
                        playerToUpdate.Username = NewUsername.Trim();
                        playerToUpdate.DefaultDifficulty = NewDefaultDifficulty;
                        playerToUpdate.Role = int.Parse(NewRole);
                        context.SaveChanges();

                        var index = Players.IndexOf(SelectedPlayer);
                        if (index >= 0)
                        {
                            Players[index] = playerToUpdate;
                        }

                        MessageBox.Show("Player updated successfully!", "Success", MessageBoxButton.OK, MessageBoxImage.Information);
                    }
                }
            }
            catch (System.Exception ex)
            {
                MessageBox.Show($"Error updating player: {ex.Message}", "Database Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }

        private void ExecuteDeletePlayer(object parameter)
        {
            if (SelectedPlayer == null) return;

            var result = MessageBox.Show($"Are you sure you want to delete the player '{SelectedPlayer.Username}'?", "Confirm Deletion", MessageBoxButton.YesNo, MessageBoxImage.Question);
            if (result == MessageBoxResult.No) return;

            try
            {
                using (var context = new HangmanHeroContext())
                {
                    var playerToDelete = context.Players.Find(SelectedPlayer.PlayerId);
                    if (playerToDelete != null)
                    {
                        context.Players.Remove(playerToDelete);
                        context.SaveChanges();
                        Players.Remove(SelectedPlayer);
                    }
                }
            }
            catch (System.Exception ex)
            {
                MessageBox.Show($"Error deleting player: {ex.Message}", "Database Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }
        #endregion
    }
}