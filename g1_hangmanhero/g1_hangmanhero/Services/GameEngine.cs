using g1_hangmanhero.Data;
using g1_hangmanhero.Models;
using System;
using System.Collections.Generic;
using System.Linq;

namespace g1_hangmanhero.Services
{
    public class GameEngine
    {
        private readonly HangmanHeroContext _context;
        private readonly Random _random = new Random();
        private Word _currentWord;
        private List<char> _guessedLetters;
        private int _lives;
        private int _score;
        private int _mistakes;
        private int _round;
        private DateTime _startTime;
        private readonly int _maxLives = 6;

        private string _gameDifficulty;
        private string _wordDifficulty;

        public GameEngine(HangmanHeroContext context)
        {
            _context = context;
            _guessedLetters = new List<char>();
            _lives = _maxLives;
            _score = 0;
            _mistakes = 0;
            _round = 0;
        }

        public bool StartGame(string gameDifficulty, string wordDifficulty, string category)
        {
            _gameDifficulty = gameDifficulty;
            _wordDifficulty = wordDifficulty;

            RandomGeneration(wordDifficulty, category);
            InitWithRevealedLetters();
            _round = 1;
            return true;
        }

        public void StartNewRound(string gameDifficulty, string wordDifficulty, string category)
        {
            _gameDifficulty = gameDifficulty;
            _wordDifficulty = wordDifficulty;

            RandomGeneration(wordDifficulty, category);
            RevealedLettersOnly();
            _round++;
        }

        private void InitWithRevealedLetters()
        {
            _guessedLetters.Clear();
            _lives = _maxLives;
            _score = 0;
            _mistakes = 0;
            _startTime = DateTime.Now;

            // Reveal letters based on player difficulty
            int revealCount = _gameDifficulty switch
            {
                "Easy" => 2,
                "Medium" => 1,
                "Hard" => 0,
                _ => 0
            };

            var distinctLetters = _currentWord.Text
                                .Where(c => Char.IsLetter(c))
                                .Select(c => Char.ToUpper(c))
                                .Distinct()
                                .ToList();
            var rand = new Random();

            for (int i = 0; i < revealCount && distinctLetters.Count > 0; i++)
            {
                int index = rand.Next(distinctLetters.Count);
                _guessedLetters.Add(distinctLetters[index]);
                distinctLetters.RemoveAt(index);
            }
        }        
        
        private void RevealedLettersOnly()
        {
            _guessedLetters.Clear();

            // Reveal letters based on player difficulty
            int revealCount = _gameDifficulty switch
            {
                "Easy" => 2,
                "Medium" => 1,
                "Hard" => 0,
                _ => 0
            };

            var distinctLetters = _currentWord.Text
                                .Where(c => Char.IsLetter(c))
                                .Select(c => Char.ToUpper(c))
                                .Distinct()
                                .ToList();
            var rand = new Random();

            for (int i = 0; i < revealCount && distinctLetters.Count > 0; i++)
            {
                int index = rand.Next(distinctLetters.Count);
                _guessedLetters.Add(distinctLetters[index]);
                distinctLetters.RemoveAt(index);
            }
        }

        private void RandomGeneration(string difficulty, string category)
        {
            var words = _context.Words
                .Where(w => w.Difficulty == difficulty && w.Category == category)
                .ToList();

            if (!words.Any()) throw new Exception("No words available for this difficulty and category.");

            _currentWord = words[_random.Next(words.Count)];
        }
        public int GetRound() => _round;

        public int AddOneMoreRound() => _round++;

        public int GetRemainingLives() => _lives;

        public int GetScore() => _score;

        public bool IsGameOver() => _lives <= 0;

        public bool isAllCorrect() => _currentWord.Text.All(c => _guessedLetters.Contains(char.ToUpper(c)));

        public bool VerifyLetterAndUpdate(char letter)
        {
            // Check if the input is not a letter or has already been guessed
            if (!char.IsLetter(letter) || _guessedLetters.Contains(char.ToUpper(letter)))
            {
                _lives--;
                _mistakes++;
                return false;
            }

            // Check if the letter exists in the current word
            letter = char.ToUpper(letter);
            _guessedLetters.Add(letter);
            bool isCorrect = _currentWord.Text.ToUpper().Contains(letter);

            if (isCorrect)
            {
                _score += CalculateScoreForCorrectGuess();
            }
            else
            {
                _lives--;
                _mistakes++;
            }

            return true;
        }

        private int CalculateScoreForCorrectGuess()
        {
            int basePoints = 10;

            int wordMultiplier = _wordDifficulty switch
            {
                "Easy" => 1,
                "Medium" => 2,
                "Hard" => 3,
                _ => 1
            };

            int gamebonus = _gameDifficulty switch
            {
                "Easy" => 1,
                "Medium" => 2,
                "Hard" => 3,
                _ => 1
            };

            return basePoints * wordMultiplier * gamebonus;
        }


        // Get the current word state (e.g., "_ _ _ a")
        public string GetCurrentWordState()
        {
            return string.Join(" ", _currentWord.Text.Select(c =>
                _guessedLetters.Contains(Char.ToUpper(c)) ? c.ToString() : "_"));
        }

        public int GetMistakes() => _mistakes;

        public DateTime GetStartTime() => _startTime;

        public Word GetCurrentWord() => _currentWord;
    }
}
