using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows;
using System.Windows.Input;
using g1_hangmanhero.Data;
using g1_hangmanhero.Models;
using g1_hangmanhero.Views;

namespace g1_hangmanhero.ViewModels
{
    public class GameSetupViewModel : ViewModelBase
    {
        private readonly Player _currentUser;
        public List<string> AvailableDifficulties { get; private set; }
        public List<string> AvailableCategories { get; private set; }
        public List<string> AvailableGameDifficulties { get; private set; }

        private readonly Window _currentWindow;

        private string _selectedDifficulty;
        public string SelectedDifficulty
        {
            get => _selectedDifficulty;
            set
            {
                _selectedDifficulty = value;
                OnPropertyChanged();
            }
        }

        private string _selectedCategory;
        public string SelectedCategory
        {
            get => _selectedCategory;
            set
            {
                _selectedCategory = value;
                OnPropertyChanged();
            }
        }

        private string _selectedGameDifficulty;
        public string SelectedGameDifficulty
        {
            get => _selectedGameDifficulty;
            set
            {
                _selectedGameDifficulty = value;
                OnPropertyChanged();
            }
        }

        public ICommand StartGameCommand { get; }

        public GameSetupViewModel(Player loggedInPlayer, Window thisWindow)
        {
            _currentUser = loggedInPlayer ?? throw new ArgumentNullException(nameof(loggedInPlayer));
            _currentWindow = thisWindow;

            StartGameCommand = new RelayCommand(ExecuteStartGame, CanExecuteStartGame);

            LoadGameOptions();
            LoadDefaultSettings();
        }

        private void LoadGameOptions()
        {
            try
            {
                using (var db = new HangmanHeroContext())
                {
                    AvailableDifficulties = db.Words
                                             .Select(w => w.Difficulty)
                                             .ToList()
                                             .Where(d => !string.IsNullOrEmpty(d))
                                             .Select(d => d.Trim())
                                             .Distinct(StringComparer.OrdinalIgnoreCase)
                                             .ToList();

                    AvailableCategories = db.Words
                                            .Select(w => w.Category)
                                            .ToList()
                                            .Where(c => !string.IsNullOrEmpty(c))
                                            .Select(c => c.Trim())
                                            .Distinct(StringComparer.OrdinalIgnoreCase)
                                            .ToList();

                    AvailableGameDifficulties = new List<string> { "Easy", "Medium", "Hard" };
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"Could not load game settings: {ex.Message}",
                               "Database Error", MessageBoxButton.OK, MessageBoxImage.Error);
                AvailableDifficulties = new List<string>();
                AvailableCategories = new List<string>();
                AvailableGameDifficulties = new List<string>();
            }
        }

        private void LoadDefaultSettings()
        {
            SelectedDifficulty = AvailableDifficulties.FirstOrDefault();
            SelectedCategory = AvailableCategories.FirstOrDefault();
            SelectedGameDifficulty = _currentUser.DefaultDifficulty ?? AvailableGameDifficulties.FirstOrDefault();
        }

        private bool CanExecuteStartGame(object parameter)
        {
            return !string.IsNullOrEmpty(SelectedDifficulty) &&
                   !string.IsNullOrEmpty(SelectedCategory) &&
                   !string.IsNullOrEmpty(SelectedGameDifficulty);
        }

        private void ExecuteStartGame(object parameter)
        {
            try
            {
                using (var db = new HangmanHeroContext())
                {
                    var wordPool = db.Words
                        .ToList()
                        .Where(w => w.Difficulty != null && w.Category != null &&
                                    string.Equals(w.Difficulty.Trim(), SelectedDifficulty, StringComparison.OrdinalIgnoreCase) &&
                                    string.Equals(w.Category.Trim(), SelectedCategory, StringComparison.OrdinalIgnoreCase))
                        .ToList();

                    if (!wordPool.Any())
                    {
                        MessageBox.Show($"No words found for Difficulty: '{SelectedDifficulty}' and Category: '{SelectedCategory}'. " +
                                        "Please check the database or select different options.",
                                        "No Words Found", MessageBoxButton.OK, MessageBoxImage.Information);
                        return;
                    }

                    _currentUser.DefaultDifficulty = SelectedGameDifficulty;
                    db.Update(_currentUser);
                    db.SaveChanges();

                    var hangmanView = new HangmanView(_currentUser, SelectedCategory, SelectedDifficulty);
                    hangmanView.Show();
                    _currentWindow.Close();
                }
            }
            catch (Exception ex)
            {
                MessageBox.Show($"An error occurred while starting the game: {ex.Message}",
                               "Error", MessageBoxButton.OK, MessageBoxImage.Error);
            }
        }
    }
}