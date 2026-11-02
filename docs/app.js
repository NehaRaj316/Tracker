// FocusCGL 31 Web Showcase Interactive Logic

// --- Study Blocks & Sub-tasks Data ---
const initialScheduleBlocks = [
  {
    id: 'b1',
    time: '10:00 AM – 1:00 PM',
    subject: 'Quantitative Aptitude',
    subtasks: [
      { id: 'st1', label: 'Algebra: Complete 40 previous year questions', done: true },
      { id: 'st2', label: 'Geometry: Review circle chord & tangent theorems', done: true },
      { id: 'st3', label: 'Arithmetic: 20 speed calculation drills', done: true },
      { id: 'st4', label: 'DI: Solve 2 bar-graph & pie-chart sets', done: false }
    ]
  },
  {
    id: 'b2',
    time: '2:00 PM – 5:00 PM',
    subject: 'Reasoning & Intelligence',
    subtasks: [
      { id: 'st5', label: 'Syllogism: Solve 30 statement-conclusion sets', done: true },
      { id: 'st6', label: 'Seating Arrangement: 4 circular & linear puzzles', done: true },
      { id: 'st7', label: 'Coding-Decoding & Number Matrix: 25 questions', done: false },
      { id: 'st8', label: 'Non-verbal: Mirror images & paper folding', done: false }
    ]
  },
  {
    id: 'b3',
    time: '6:00 PM – 8:30 PM',
    subject: 'English Comprehension',
    subtasks: [
      { id: 'st9', label: 'Grammar: 25 Spotting Errors & Sentence Improvements', done: true },
      { id: 'st10', label: 'Vocabulary: 50 Idioms, Phrases & One Word Subs', done: false },
      { id: 'st11', label: 'Comprehension: 2 Cloze Tests & 1 Reading Passage', done: false }
    ]
  },
  {
    id: 'b4',
    time: '9:30 PM – 10:00 PM',
    subject: 'Daily Review & GA',
    subtasks: [
      { id: 'st12', label: 'Review today’s revision statuses (Mastered / Review)', done: false }
    ]
  }
];

// --- 31-Day Topics Dataset ---
const initialTopics = [
  { id: 1, name: 'Algebra & Polynomials', subject: 'QUANT', day: 1, status: 'MASTERED' },
  { id: 2, name: 'Geometry: Triangles & Congruence', subject: 'QUANT', day: 2, status: 'MASTERED' },
  { id: 3, name: 'Trigonometry & Heights/Distances', subject: 'QUANT', day: 3, status: 'NEEDS_REVIEW' },
  { id: 4, name: 'Profit, Loss & Discount', subject: 'QUANT', day: 4, status: 'MASTERED' },
  { id: 5, name: 'Time, Speed & Distance / Trains', subject: 'QUANT', day: 5, status: 'NEEDS_REVIEW' },
  { id: 6, name: 'Number Systems & Divisibility', subject: 'QUANT', day: 6, status: 'MASTERED' },
  { id: 7, name: 'Data Interpretation: Bar & Tables', subject: 'QUANT', day: 7, status: 'NOT_STARTED' },
  
  { id: 8, name: 'Syllogisms (Possibility & Only A Few)', subject: 'REASONING', day: 1, status: 'MASTERED' },
  { id: 9, name: 'Circular & Linear Seating Arrangement', subject: 'REASONING', day: 2, status: 'MASTERED' },
  { id: 10, name: 'Blood Relations & Family Trees', subject: 'REASONING', day: 3, status: 'MASTERED' },
  { id: 11, name: 'Analogy & Number Classification', subject: 'REASONING', day: 4, status: 'NOT_STARTED' },
  { id: 12, name: 'Statement & Assumptions / Arguments', subject: 'REASONING', day: 5, status: 'NEEDS_REVIEW' },

  { id: 13, name: 'Subject-Verb Agreement Rules', subject: 'ENGLISH', day: 1, status: 'MASTERED' },
  { id: 14, name: 'Tenses, Conditionals & Inversion', subject: 'ENGLISH', day: 2, status: 'MASTERED' },
  { id: 15, name: 'Active & Passive Voice Transformations', subject: 'ENGLISH', day: 3, status: 'MASTERED' },
  { id: 16, name: 'Direct & Indirect Speech (Narration)', subject: 'ENGLISH', day: 4, status: 'NEEDS_REVIEW' },
  { id: 17, name: 'High-Frequency Idioms & Phrases', subject: 'ENGLISH', day: 5, status: 'NEEDS_REVIEW' },
  { id: 18, name: 'Cloze Test & Contextual Vocabulary', subject: 'ENGLISH', day: 6, status: 'NOT_STARTED' },

  { id: 19, name: 'Indian Constitution: Articles & Schedules', subject: 'GA', day: 1, status: 'MASTERED' },
  { id: 20, name: 'Modern Indian History: Freedom Movement', subject: 'GA', day: 2, status: 'MASTERED' },
  { id: 21, name: 'Indian Geography: River Systems & Passes', subject: 'GA', day: 3, status: 'NEEDS_REVIEW' },
  { id: 22, name: 'General Science: Biology & Human Anatomy', subject: 'GA', day: 4, status: 'MASTERED' },
  { id: 23, name: 'Macroeconomics & Budget Concepts', subject: 'GA', day: 5, status: 'NOT_STARTED' },
  { id: 24, name: 'Annual Current Affairs: Awards & Summits', subject: 'GA', day: 6, status: 'NEEDS_REVIEW' }
];

let scheduleData = JSON.parse(JSON.stringify(initialScheduleBlocks));
let topicsData = JSON.parse(JSON.stringify(initialTopics));
let activeSubjectFilter = 'ALL';

// --- Initialize DOM and Events ---
document.addEventListener('DOMContentLoaded', () => {
  setupNavigation();
  setupTabs();
  renderSchedule();
  setupPomodoro();
  renderTopics();
  renderDaysMatrix();
});

// Mobile menu toggle
function setupNavigation() {
  const toggle = document.getElementById('menuToggle');
  const links = document.querySelector('.nav-links');
  if (toggle && links) {
    toggle.addEventListener('click', () => {
      links.classList.toggle('active');
    });
  }
}

// Simulator Tabs
function setupTabs() {
  const tabs = document.querySelectorAll('.sim-tab');
  const panels = document.querySelectorAll('.sim-panel');

  tabs.forEach(tab => {
    tab.addEventListener('click', () => {
      tabs.forEach(t => t.classList.remove('active'));
      panels.forEach(p => p.classList.remove('active'));

      tab.classList.add('active');
      const targetId = `tab-${tab.getAttribute('data-tab')}`;
      const targetPanel = document.getElementById(targetId);
      if (targetPanel) {
        targetPanel.classList.add('active');
      }
    });
  });
}

// Render Schedule & Sub-tasks
function renderSchedule() {
  const container = document.getElementById('scheduleBlocksContainer');
  if (!container) return;

  container.innerHTML = '';
  let totalSubtasks = 0;
  let doneSubtasks = 0;

  scheduleData.forEach(block => {
    const card = document.createElement('div');
    card.className = 'sim-block-card';

    const blockTotal = block.subtasks.length;
    const blockDone = block.subtasks.filter(s => s.done).length;
    const pct = blockTotal > 0 ? Math.round((blockDone / blockTotal) * 100) : 0;

    totalSubtasks += blockTotal;
    doneSubtasks += blockDone;

    let subtasksHtml = block.subtasks.map(st => `
      <label class="subtask-item ${st.done ? 'checked' : ''}">
        <input type="checkbox" data-block="${block.id}" data-subtask="${st.id}" ${st.done ? 'checked' : ''}>
        <span>${st.label}</span>
      </label>
    `).join('');

    card.innerHTML = `
      <div class="sim-block-head">
        <span class="sim-block-title">${block.subject}</span>
        <span class="sim-block-time">${block.time}</span>
      </div>
      <div class="progress-bar-wrap">
        <div class="progress-bar-fill" style="width: ${pct}%"></div>
      </div>
      <div class="subtasks-list">
        ${subtasksHtml}
      </div>
      <div class="block-progress-footer">
        <span>${blockDone} of ${blockTotal} sub-tasks</span>
        <strong>${pct}% Done</strong>
      </div>
    `;

    container.appendChild(card);
  });

  // Attach checkbox listeners
  container.querySelectorAll('input[type="checkbox"]').forEach(cb => {
    cb.addEventListener('change', (e) => {
      const blockId = e.target.getAttribute('data-block');
      const subtaskId = e.target.getAttribute('data-subtask');
      const block = scheduleData.find(b => b.id === blockId);
      if (block) {
        const sub = block.subtasks.find(s => s.id === subtaskId);
        if (sub) {
          sub.done = e.target.checked;
          renderSchedule();
        }
      }
    });
  });

  // Update Summary Card
  const totalPercent = totalSubtasks > 0 ? Math.round((doneSubtasks / totalSubtasks) * 100) : 0;
  const percentEl = document.getElementById('dailyProgressPercent');
  const doneEl = document.getElementById('completedSubtasksCount');
  const totalEl = document.getElementById('totalSubtasksCount');
  const ringEl = document.getElementById('dailyProgressRing');

  if (percentEl) percentEl.textContent = `${totalPercent}%`;
  if (doneEl) doneEl.textContent = doneSubtasks;
  if (totalEl) totalEl.textContent = totalSubtasks;

  if (ringEl) {
    const circumference = 314; // 2 * pi * 50
    const offset = circumference - (circumference * totalPercent) / 100;
    ringEl.style.strokeDashoffset = offset;
  }
}

// --- Pomodoro Timer Engine ---
let pomoDuration = 25 * 60;
let pomoRemaining = 25 * 60;
let pomoTimerId = null;
let pomoRunning = false;
let pomoIntervalsCompleted = 4;

function setupPomodoro() {
  const display = document.getElementById('pomoTime');
  const startBtn = document.getElementById('pomoStartBtn');
  const resetBtn = document.getElementById('pomoResetBtn');
  const statusEl = document.getElementById('pomoStatus');
  const modeButtons = document.querySelectorAll('.pomo-mode-btn');

  function updateDisplay() {
    const mins = Math.floor(pomoRemaining / 60);
    const secs = pomoRemaining % 60;
    display.textContent = `${String(mins).padStart(2, '0')}:${String(secs).padStart(2, '0')}`;
  }

  modeButtons.forEach(btn => {
    btn.addEventListener('click', () => {
      if (pomoRunning) {
        clearInterval(pomoTimerId);
        pomoRunning = false;
        startBtn.textContent = 'Start Focus';
      }
      modeButtons.forEach(b => b.classList.remove('active'));
      btn.classList.add('active');

      const minutes = parseInt(btn.getAttribute('data-mode'), 10);
      pomoDuration = minutes * 60;
      pomoRemaining = pomoDuration;
      updateDisplay();

      if (minutes === 5 || minutes === 15) {
        statusEl.textContent = 'Break Time: Refresh & hydrate';
      } else {
        statusEl.textContent = 'Ready for Focused Study Block';
      }
    });
  });

  startBtn.addEventListener('click', () => {
    if (!pomoRunning) {
      pomoRunning = true;
      startBtn.textContent = 'Pause Focus';
      statusEl.textContent = 'Focus in progress... Stay disciplined!';

      pomoTimerId = setInterval(() => {
        if (pomoRemaining > 0) {
          pomoRemaining--;
          updateDisplay();
        } else {
          clearInterval(pomoTimerId);
          pomoRunning = false;
          startBtn.textContent = 'Start Focus';
          pomoIntervalsCompleted++;
          document.getElementById('pomoIntervalCount').textContent = pomoIntervalsCompleted;
          statusEl.textContent = '🎉 Interval Completed! Take a short rest.';
          playChime();
        }
      }, 1000);
    } else {
      clearInterval(pomoTimerId);
      pomoRunning = false;
      startBtn.textContent = 'Resume Focus';
      statusEl.textContent = 'Paused';
    }
  });

  resetBtn.addEventListener('click', () => {
    clearInterval(pomoTimerId);
    pomoRunning = false;
    startBtn.textContent = 'Start Focus';
    pomoRemaining = pomoDuration;
    updateDisplay();
    statusEl.textContent = 'Timer Reset';
  });

  updateDisplay();
}

function playChime() {
  try {
    const ctx = new (window.AudioContext || window.webkitAudioContext)();
    const osc = ctx.createOscillator();
    const gain = ctx.createGain();
    osc.connect(gain);
    gain.connect(ctx.destination);
    osc.type = 'sine';
    osc.frequency.setValueAtTime(587.33, ctx.currentTime); // D5
    gain.gain.setValueAtTime(0.2, ctx.currentTime);
    gain.gain.exponentialRampToValueAtTime(0.001, ctx.currentTime + 1.2);
    osc.start();
    osc.stop(ctx.currentTime + 1.2);
  } catch (e) {
    // AudioContext fallback
  }
}

// --- Topic Revision Tracker ---
function renderTopics() {
  const container = document.getElementById('topicsContainer');
  if (!container) return;

  container.innerHTML = '';

  const filterBtns = document.querySelectorAll('.sub-filter-btn');
  filterBtns.forEach(btn => {
    btn.onclick = () => {
      filterBtns.forEach(b => b.classList.remove('active'));
      btn.classList.add('active');
      activeSubjectFilter = btn.getAttribute('data-subject');
      renderTopics();
    };
  });

  const filtered = activeSubjectFilter === 'ALL'
    ? topicsData
    : topicsData.filter(t => t.subject === activeSubjectFilter);

  filtered.forEach(topic => {
    const card = document.createElement('div');
    card.className = 'topic-card';

    card.innerHTML = `
      <div class="topic-card-header">
        <div>
          <span class="block-tag ${topic.subject.toLowerCase()}">${topic.subject}</span>
          <h5 style="margin-top: 6px;">${topic.name}</h5>
        </div>
        <span class="day-badge">Day ${topic.day}</span>
      </div>
      <div class="topic-status-buttons">
        <button class="status-btn ${topic.status === 'MASTERED' ? 'selected-mastered' : ''}" data-id="${topic.id}" data-status="MASTERED">
          Mastered
        </button>
        <button class="status-btn ${topic.status === 'NEEDS_REVIEW' ? 'selected-review' : ''}" data-id="${topic.id}" data-status="NEEDS_REVIEW">
          Needs Review
        </button>
        <button class="status-btn ${topic.status === 'NOT_STARTED' ? 'selected-notstarted' : ''}" data-id="${topic.id}" data-status="NOT_STARTED">
          Not Started
        </button>
      </div>
    `;

    container.appendChild(card);
  });

  // Attach status change events
  container.querySelectorAll('.status-btn').forEach(btn => {
    btn.addEventListener('click', (e) => {
      const topicId = parseInt(e.target.getAttribute('data-id'), 10);
      const newStatus = e.target.getAttribute('data-status');
      const topic = topicsData.find(t => t.id === topicId);
      if (topic) {
        topic.status = newStatus;
        renderTopics();
        updateRevisionSummaryCounters();
      }
    });
  });

  updateRevisionSummaryCounters();
}

function updateRevisionSummaryCounters() {
  const mastered = topicsData.filter(t => t.status === 'MASTERED').length;
  const review = topicsData.filter(t => t.status === 'NEEDS_REVIEW').length;
  const notStarted = topicsData.filter(t => t.status === 'NOT_STARTED').length;

  const mEl = document.getElementById('countMastered');
  const rEl = document.getElementById('countReview');
  const nEl = document.getElementById('countNotStarted');

  if (mEl) mEl.textContent = `Mastered: ${mastered}`;
  if (rEl) rEl.textContent = `Needs Review: ${review}`;
  if (nEl) nEl.textContent = `Not Started: ${notStarted}`;
}

// --- 31-Day Streak Matrix ---
function renderDaysMatrix() {
  const container = document.getElementById('daysMatrixContainer');
  if (!container) return;

  container.innerHTML = '';
  const currentDay = 14;

  for (let d = 1; d <= 31; d++) {
    const cell = document.createElement('div');
    cell.className = 'day-cell';
    cell.textContent = `D${d}`;

    if (d < currentDay) {
      cell.classList.add('done');
      cell.title = `Day ${d}: All study blocks completed`;
    } else if (d === currentDay) {
      cell.classList.add('current');
      cell.title = `Day ${d}: Today (In Progress)`;
    } else {
      cell.classList.add('upcoming');
      cell.title = `Day ${d}: Upcoming`;
    }

    container.appendChild(cell);
  }
}
