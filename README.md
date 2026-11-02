# FocusCGL 31 — 31-Day SSC CGL Preparation & Study Planner

[![Deploy to GitHub Pages](https://github.com/actions/workflows/deploy-pages.yml/badge.svg)](https://github.com/actions/workflows/deploy-pages.yml)
[![Platform](https://img.shields.io/badge/Platform-Android%208.0%2B-green.svg)](https://developer.android.com)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose%20M3-blue.svg)](https://developer.android.com/jetpack/compose)
[![Database](https://img.shields.io/badge/Storage-Room%20SQLite%20(Offline)-orange.svg)](https://developer.android.com/training/data-storage/room)

A native Android application built for SSC CGL aspirants to execute a disciplined **31-Day Preparation & Revision Roadmap** based on a rigid **10:00 AM to 10:00 PM** daily schedule.

---

## 🌟 Key Features

1. **Strict 10 AM – 10 PM Schedule Architecture**:
   - **10:00 AM – 1:00 PM**: Quantitative Aptitude (Algebra, Geometry, Trigonometry, Arithmetic, DI)
   - **1:00 PM – 2:00 PM**: Lunch & Mental Rest
   - **2:00 PM – 5:00 PM**: Reasoning & General Intelligence (Syllogisms, Puzzles, Coding-Decoding)
   - **5:00 PM – 6:00 PM**: Physical Activity & Outdoor Evening Walk
   - **6:00 PM – 8:30 PM**: English Comprehension & Grammar Drills
   - **8:30 PM – 9:30 PM**: Dinner
   - **9:30 PM – 10:00 PM**: Daily Review, GA Revision & Next Day Roadmap Setup

2. **31-Day Continuous Streak Visualizer**:
   - Circular progress ring on the dashboard showing consecutive completed days.
   - 31-day matrix tracking daily block completion.

3. **Sub-Task Tracking with Completion Percentage**:
   - Break down every 3-hour study block into concrete, manageable micro-goals.
   - Real-time progress indicators reflecting completion status.

4. **Exam-Tuned Pomodoro Timer**:
   - Preset modes for 25-minute sprints, 50-minute deep-focus simulations, and 5-min / 15-min breaks.
   - Built specifically to match the 10 AM – 10 PM study block intervals.

5. **Subject Revision Tracker**:
   - 124 curated topics across all 4 subjects (Quant, Reasoning, English, GA).
   - Fast status tagging: `Mastered`, `Needs Review`, and `Not Started`.
   - Live subject mastery progress bars and revision summary counters.

6. **Full Mock Test Score Progression**:
   - Track Tier 1 / Tier 2 scores out of 200 with cutoff clearance indicators.

7. **100% Offline & Private**:
   - Powered by Android Jetpack Room SQLite. Zero accounts, zero ads, zero external telemetry.

---

## 🚀 Live Web App Clone on GitHub Pages

The `/docs` directory contains a full, standalone, and responsive **web app clone** of `FocusCGL 31`. It runs completely in any modern web browser on desktop and mobile:
- **10 AM – 10 PM Interactive Daily Schedule**: All 13 continuous daily blocks, customizable sub-tasks per block, live current time indicator on the Gantt timeline, and day reset/completion.
- **Dedicated Pomodoro Timer**: 25m focus, 50m deep focus, full block, and 5m/15m breaks with Web Audio API chime synthesis.
- **31-Day Study Streak Tracker**: Live SVG circular progress ring with 31 daily graduation ticks, continuity ribbon, and streak metrics.
- **Topic Revision Tracker**: 124 official syllabus topics across Quant, Reasoning, English, and GA with search, filtering, and 1-click status tagging (`Mastered`, `Needs Review`, `Not Started`).
- **Mock Test Score Progression**: 31 full-length mock tracker with sectional scores (/50 each), 170+ cutoff tracker, and responsive SVG trend chart.
- **Formula & Rule Flashcards**: High-yield formulas, reverse alphabet pairs, and golden grammar rules.
- **Full Data Persistence**: Uses browser `localStorage` so all streak, subtask, topic, and mock scores persist across browser refreshes.
- **Direct APK Download**: One-click download button for `FocusCGL-31.apk` (23 MB).

### Enabling GitHub Pages in Your Repository:

You can activate GitHub Pages in your GitHub repository in two ways:

#### Option A: GitHub Actions (Recommended)
1. In your GitHub repository, navigate to **Settings** > **Pages** (under Code and automation).
2. Under **Build and deployment** > **Source**, select **GitHub Actions**.
3. Push to `main` or trigger the workflow under **Actions** > **Deploy to GitHub Pages** > **Run workflow**.
4. The workflow automatically builds the Android APK, stages the site from `docs/`, and publishes the live web app.

#### Option B: Deploy from Branch
1. Navigate to **Settings** > **Pages**.
2. Under **Build and deployment** > **Source**, choose **Deploy from a branch**.
3. Under **Branch**, select `main` and folder `/docs`.
4. Click **Save**. Within 1–2 minutes, your web app clone will be live at `https://<username>.github.io/<repo-name>/`.

---

## 📱 Building the Native Android App Locally

```bash
# Clone the repository
git clone https://github.com/<username>/<repo-name>.git
cd <repo-name>

# Build the debug APK
gradle assembleDebug

# Run unit and Robolectric tests
gradle :app:testDebugUnitTest
```

The compiled APK will be located at `app/build/outputs/apk/debug/app-debug.apk`.
