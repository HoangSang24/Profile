using g1_hangmanhero.Data;
using g1_hangmanhero.Models;
using g1_hangmanhero.Services;
using g1_hangmanhero.Views;
using Microsoft.EntityFrameworkCore;
using System;
using System.Linq;
using System.Windows;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;

namespace g1_hangmanhero.ViewModels
{
    public class HangmanViewModel : ViewModelBase
    {
        private readonly Player _currentUser;
        private readonly GameEngine _gameEngine;
        private readonly HangmanHeroContext _context;
        private readonly Window _currentWindow;

        public HangmanViewModel(Player loggedInPlayer, string category, string wordDifficulty, Window currentWindow)
        {
            _currentUser = loggedInPlayer ?? throw new ArgumentNullException(nameof(loggedInPlayer));

            _context = new HangmanHeroContext();
            _gameEngine = new GameEngine(_context);

            _username = _currentUser.Username;
            _topic = category;
            _wordDifficulty = wordDifficulty;
            _gameDifficulty = _currentUser.DefaultDifficulty;
            _currentWindow = currentWindow;

            StartNewGame();
        }

        private string _username;
        public string Username
        {
            get { return _username; }
            set { _username = value; OnPropertyChanged(); }
        }


        private string _topic;
        public string Topic
        {
            get { return _topic; }
            set { _topic = value; OnPropertyChanged(); }
        }        
        
        private int _round;
        public int Round
        {
            get { return _round; }
            set { _round = value; OnPropertyChanged(); }
        }

        private string _guessedLetter;
        public string GuessedLetter
        {
            get { return _guessedLetter; }
            set
            {
                _guessedLetter = value;
                OnPropertyChanged();
                OnGuess();
            }
        }

        private string _currentWordState;
        public string CurrentWordState
        {
            get { return _currentWordState; }
            set { _currentWordState = value; OnPropertyChanged(); }
        }

        private int _score;
        public int Score
        {
            get { return _score; }
            set { _score = value; OnPropertyChanged(); }
        }

        private bool _isGameOver;
        public bool IsGameOver
        {
            get { return _isGameOver; }
            set { _isGameOver = value; OnPropertyChanged(); }
        }

        private string _gameDifficulty;
        public string GameDifficulty
        {
            get { return _gameDifficulty; }
            set { _gameDifficulty = value; OnPropertyChanged(); }
        }        
        
        private string _wordDifficulty;
        public string WordDifficulty
        {
            get { return _wordDifficulty; }
            set { _wordDifficulty = value; OnPropertyChanged(); }
        }


        private int _remainingLives;
        public int RemainingLives
        {
            get { return _remainingLives; }
            set
            {
                _remainingLives = value;
                OnPropertyChanged();

                // Update the visibility of hangman images based on remaining lives
                UpdateHangmanImage();
            }
        }

        private ImageSource _hangmanImageSource;
        public ImageSource HangmanImageSource
        {
            get { return _hangmanImageSource; }
            set { _hangmanImageSource = value; OnPropertyChanged(); }
        }

        private void OnGuess()
        {
            // Skip if GuessedLetter is empty (e.g., during initialization)
            if (string.IsNullOrEmpty(GuessedLetter))
            {
                return;
            }

            // Validate input
            if (GuessedLetter.Length != 1 || !char.IsLetter(GuessedLetter[0]))
            {
                MessageBox.Show("Please enter a single valid letter (A-Z).", "Invalid Input", MessageBoxButton.OK, MessageBoxImage.Warning);
                GuessedLetter = string.Empty;
                return;
            }

            char letter = char.ToUpper(GuessedLetter[0]);
            if (!_gameEngine.VerifyLetterAndUpdate(letter))
            {
                GuessedLetter = string.Empty;
            }

            CurrentWordState = _gameEngine.GetCurrentWordState();
            RemainingLives = _gameEngine.GetRemainingLives();
            Score = _gameEngine.GetScore();
            GuessedLetter = string.Empty;

            // Check if word is complete before checking game over
            if (_gameEngine.isAllCorrect())
            {
                StartNewRound();
            }
            else if (_gameEngine.IsGameOver())
            {
                SaveGameResult();
            }
        }

        private void StartNewRound()
        {
            _gameEngine.StartNewRound(_gameDifficulty, _wordDifficulty, _topic);  // Start new round with new word
            CurrentWordState = _gameEngine.GetCurrentWordState();
            RemainingLives = _gameEngine.GetRemainingLives();
            Score = _gameEngine.GetScore();
            Round = _gameEngine.GetRound();
            IsGameOver = false;
            GuessedLetter = string.Empty;
        }

        private void SaveGameResult()
        {
            // Ensure that _currentUser is tracked in the current context
            var player = _context.Players.FirstOrDefault(p => p.PlayerId == _currentUser.PlayerId);
            if (player != null)
            {
                // Create the GameHistory object
                var gameHistory = new GameHistory
                {
                    Player = player,  // Use the tracked player from the context
                    Word = _gameEngine.GetCurrentWord(),
                    Score = Score,
                    Mistakes = _gameEngine.GetMistakes(),
                    TimeTaken = (int)(DateTime.Now - _gameEngine.GetStartTime()).TotalSeconds,
                    PlayedAt = DateTime.Now
                };

                // Add the game history to the context and save changes
                _context.GameHistories.Add(gameHistory);

                try
                {
                    _context.SaveChanges();
                }
                catch (DbUpdateException ex)
                {
                    // Handle the error (for example, log it)
                    MessageBox.Show($"Error saving game result: {ex.Message}", "Error", MessageBoxButton.OK, MessageBoxImage.Error);
                    return;
                }

                // Pass the saved game history to the summary view
                var summaryView = new SummaryView(gameHistory);  // Pass the generated GameId
                summaryView.Show();
                _currentWindow.Close();
            }
        }

        private void StartNewGame()
        {
            bool started = _gameEngine.StartGame(_gameDifficulty ,_wordDifficulty, _topic);
            if (!started)
            {
                IsGameOver = true;
                return;
            }

            CurrentWordState = _gameEngine.GetCurrentWordState();
            RemainingLives = _gameEngine.GetRemainingLives();
            Score = _gameEngine.GetScore();
            Round = _gameEngine.GetRound();
            IsGameOver = false;
            GuessedLetter = string.Empty;
        }

        private void UpdateHangmanImage()
        {
            // Define the path for the images stored as resources
            string baseImagePath = "pack://application:,,,/Images/hangman";

            // Map the remaining lives to the appropriate hangman image
            string imageFileName = $"{baseImagePath}{6 - RemainingLives}.png";

            // Set the ImageSource property to load the image from resources
            HangmanImageSource = new BitmapImage(new Uri(imageFileName));
        }
    }
}
