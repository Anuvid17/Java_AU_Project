/**
 * QuizMaster - Quiz Timer & Navigation Logic
 *
 * Handles:
 * - Countdown timer with auto-submit
 * - Question navigation (previous/next)
 * - Progress tracking
 * - LocalStorage backup for timer state
 */

// ===== Global State =====
let currentQuestionIndex = 0;
let timerInterval = null;
let remainingSeconds = 0;
let startTime = 0;

/**
 * Initialize the quiz timer and navigation.
 * Called when the quiz attempt page loads.
 */
function initQuiz() {
    // Check if we are on the quiz attempt page
    if (typeof totalQuestions === 'undefined' || typeof durationSeconds === 'undefined') {
        return; // Not on quiz page
    }

    remainingSeconds = durationSeconds;
    startTime = Date.now();

    // Try to restore timer from localStorage (in case of page refresh)
    const savedState = loadTimerState();
    if (savedState && savedState.quizId === quizId) {
        const elapsed = Math.floor((Date.now() - savedState.startTime) / 1000);
        remainingSeconds = Math.max(0, durationSeconds - elapsed);
        startTime = savedState.startTime;
    } else {
        saveTimerState();
    }

    // Start countdown
    updateTimerDisplay();
    timerInterval = setInterval(function () {
        remainingSeconds--;
        updateTimerDisplay();

        if (remainingSeconds <= 0) {
            clearInterval(timerInterval);
            clearTimerState();
            autoSubmit();
        }

        // Flash timer when less than 60 seconds
        if (remainingSeconds <= 60) {
            document.getElementById('quizTimer').classList.add('timer-critical');
        }
    }, 1000);

    // Update progress
    updateProgress();
    updateNavButtons();
}

/**
 * Update the timer display with remaining time.
 */
function updateTimerDisplay() {
    const minutes = Math.floor(remainingSeconds / 60);
    const seconds = remainingSeconds % 60;
    const display = String(minutes).padStart(2, '0') + ':' + String(seconds).padStart(2, '0');
    const timerEl = document.getElementById('timerDisplay');
    if (timerEl) {
        timerEl.textContent = display;
    }
}

/**
 * Navigate to a different question.
 * @param {number} direction - 1 for next, -1 for previous
 */
function navigateQuestion(direction) {
    const newIndex = currentQuestionIndex + direction;
    if (newIndex < 0 || newIndex >= totalQuestions) return;

    goToQuestion(newIndex);
}

/**
 * Jump to a specific question by index.
 * @param {number} index - 0-based question index
 */
function goToQuestion(index) {
    if (index < 0 || index >= totalQuestions) return;

    // Hide current question
    const currentEl = document.getElementById('question-' + currentQuestionIndex);
    if (currentEl) currentEl.style.display = 'none';

    // Show target question
    const targetEl = document.getElementById('question-' + index);
    if (targetEl) {
        targetEl.style.display = 'block';
        targetEl.classList.add('animate-fade-in');
    }

    currentQuestionIndex = index;
    updateNavButtons();
    updateProgress();
    updateQuestionDots();
}

/**
 * Update navigation button states.
 */
function updateNavButtons() {
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');
    const submitBtn = document.getElementById('submitBtn');

    if (prevBtn) prevBtn.disabled = (currentQuestionIndex === 0);

    if (nextBtn && submitBtn) {
        if (currentQuestionIndex === totalQuestions - 1) {
            nextBtn.style.display = 'none';
            submitBtn.style.display = 'inline-block';
        } else {
            nextBtn.style.display = 'inline-block';
            submitBtn.style.display = 'none';
        }
    }
}

/**
 * Update the progress bar.
 */
function updateProgress() {
    const progressBar = document.getElementById('progressBar');
    const currentDisplay = document.getElementById('currentQuestion');

    if (progressBar) {
        const percent = ((currentQuestionIndex + 1) / totalQuestions) * 100;
        progressBar.style.width = percent + '%';
    }

    if (currentDisplay) {
        currentDisplay.textContent = currentQuestionIndex + 1;
    }
}

/**
 * Update question navigator dots.
 */
function updateQuestionDots() {
    const dots = document.querySelectorAll('.question-dot');
    dots.forEach(function (dot, index) {
        dot.classList.remove('active');
        if (index === currentQuestionIndex) {
            dot.classList.add('active');
        }

        // Check if question has been answered
        const questionCard = document.getElementById('question-' + index);
        if (questionCard) {
            const checkedInput = questionCard.querySelector('input:checked');
            if (checkedInput) {
                dot.classList.add('answered');
            }
        }
    });
}

/**
 * Confirm quiz submission.
 * @returns {boolean} Whether to proceed with submission
 */
function confirmSubmit() {
    // Count answered questions
    let answered = 0;
    for (let i = 0; i < totalQuestions; i++) {
        const card = document.getElementById('question-' + i);
        if (card && card.querySelector('input:checked')) {
            answered++;
        }
    }

    const unanswered = totalQuestions - answered;
    let message = 'Are you sure you want to submit the quiz?';
    if (unanswered > 0) {
        message = 'You have ' + unanswered + ' unanswered question(s). Are you sure you want to submit?';
    }

    if (confirm(message)) {
        setTimeTaken();
        clearTimerState();
        return true;
    }
    return false;
}

/**
 * Auto-submit the quiz when timer expires.
 */
function autoSubmit() {
    alert('⏰ Time\'s up! Your quiz will be submitted automatically.');
    setTimeTaken();
    clearTimerState();
    document.getElementById('quizForm').submit();
}

/**
 * Calculate and set time taken in the hidden input.
 */
function setTimeTaken() {
    const timeTaken = Math.floor((Date.now() - startTime) / 1000);
    const input = document.getElementById('timeTakenInput');
    if (input) {
        input.value = Math.min(timeTaken, durationSeconds);
    }
}

// ===== LocalStorage Timer Backup =====

function saveTimerState() {
    try {
        localStorage.setItem('quizTimer', JSON.stringify({
            quizId: quizId,
            startTime: startTime,
            duration: durationSeconds
        }));
    } catch (e) { /* ignore */ }
}

function loadTimerState() {
    try {
        const state = localStorage.getItem('quizTimer');
        return state ? JSON.parse(state) : null;
    } catch (e) {
        return null;
    }
}

function clearTimerState() {
    try {
        localStorage.removeItem('quizTimer');
    } catch (e) { /* ignore */ }
}

// ===== Track Answered Questions =====

// Listen for option selection to update dots
document.addEventListener('change', function (e) {
    if (e.target.type === 'radio' && e.target.name && e.target.name.startsWith('answer_')) {
        updateQuestionDots();
    }
});

// ===== Initialize on DOM Ready =====
document.addEventListener('DOMContentLoaded', function () {
    initQuiz();
});
