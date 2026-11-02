/**
 * FocusCGL 31 — Comprehensive Web Application Architecture
 * Fully functional client-side clone of the FocusCGL 31 Android app.
 * State is managed reactively and persisted via localStorage.
 */

// 1. DEFAULT DATASETS (Mirroring InitialData.kt & RevisionData.kt)
const DEFAULT_DAY_PLANS = [
  // Phase 1: High-Weightage Core Foundations (Days 1 - 10)
  {
    dayNumber: 1,
    phase: 1,
    title: "Percentages, Number Series & Ancient History",
    quantTopic: "Percentage: Fraction-to-percentage values (1/2 to 1/20), % increase/decrease, successive percentage change (a+b+ab/100). Solve 40 Qs.",
    reasoningTopic: "Analogy & Number Series: Prime number patterns, cubes/squares difference series, alphabet positional analogies. Solve 40 Qs.",
    englishTopic: "Grammar: Subject-Verb Agreement (Rules 1-10: 'either..or', 'neither..nor', collective nouns). Vocab: 50 high-frequency words + 20 Idioms.",
    gaTopic: "Ancient History: Indus Valley Civilization (sites, findings) & Vedic Culture. Current Affairs: Last Month national appointments & summits.",
    mockTestName: "Full Mock Test #1 (Tier-1 Diagnostic Assessment)"
  },
  {
    dayNumber: 2,
    phase: 1,
    title: "Profit & Loss, Coding-Decoding & Medieval India",
    quantTopic: "Profit, Loss & Discount: Cost price, selling price, marked price, successive discounts, dishonest dealer concept. Solve 40 Qs.",
    reasoningTopic: "Coding & Decoding: Letter coding (+/- shifts), number substitution, deciphering message codes. Solve 40 Qs.",
    englishTopic: "Grammar: Tenses (Simple vs Perfect, 'since/for', conditional sentences). Vocab: 50 words + One-Word Substitutions (OWS 1-30).",
    gaTopic: "Medieval History: Delhi Sultanate (Slave, Khilji, Tughlaq dynasties) & Mughal Empire administration. Current Affairs: Defence exercises & schemes.",
    mockTestName: "Full Mock Test #2 (Focus on Speed & Accuracy)"
  },
  {
    dayNumber: 3,
    phase: 1,
    title: "Ratio & Proportion, Syllogisms & Modern History",
    quantTopic: "Ratio, Proportion & Partnership: Duplicate/sub-duplicate ratios, mean proportional, profit distribution based on time & capital. Solve 35 Qs.",
    reasoningTopic: "Syllogisms: All, Some, No, Some-Not cases, Venn diagram method, Possibility conditions. Solve 40 Qs.",
    englishTopic: "Voice: Active & Passive Voice conversion rules for all tenses and interrogative sentences. Practice 30 transformations.",
    gaTopic: "Modern India: Revolt of 1857, Governor Generals/Viceroys, Indian National Congress sessions & key resolutions.",
    mockTestName: "Full Mock Test #3 (Sectional Time Management Drill)"
  },
  {
    dayNumber: 4,
    phase: 1,
    title: "Simple & Compound Interest, Blood Relations & Constitution",
    quantTopic: "SI & CI: Difference between CI and SI for 2 & 3 years, half-yearly/quarterly compounding, installment problems. Solve 35 Qs.",
    reasoningTopic: "Blood Relations: Family tree drawing method, coded blood relations (A+B means A is mother of B). Solve 35 Qs.",
    englishTopic: "Narration: Direct and Indirect Speech transformation rules (reporting verbs, tense shifts, pronoun changes). Practice 30 Qs.",
    gaTopic: "Indian Polity: Making of Constitution, Preamble, Fundamental Rights (Articles 12-35) & Fundamental Duties (Article 51A).",
    mockTestName: "Full Mock Test #4 (Target: Cut Down Unforced Errors)"
  },
  {
    dayNumber: 5,
    phase: 1,
    title: "Time & Work, Direction Sense & Indian Parliament",
    quantTopic: "Time & Work: LCM efficiency method, alternative day working, wages distribution, negative work (Pipes & Cisterns). Solve 40 Qs.",
    reasoningTopic: "Direction Sense & Distance: Pythagoras theorem applications, shadow angles (sunrise/sunset), turns & displacements. Solve 35 Qs.",
    englishTopic: "Grammar: Prepositions (Fixed prepositions list: 'accused of', 'prohibit from', 'senior to') + 2 Cloze Tests.",
    gaTopic: "Indian Polity: Parliament (Lok Sabha, Rajya Sabha), President's powers, ordinances, Constitutional Amendments (42nd, 44th, 73rd, 86th).",
    mockTestName: "Full Mock Test #5 (Milestone: First 5-Mock Review)"
  },
  {
    dayNumber: 6,
    phase: 1,
    title: "Speed, Time & Distance, Order & Ranking & Physical Geography",
    quantTopic: "Time, Speed & Distance: Relative speed, trains crossing poles/platforms, average speed (2xy/(x+y)), boats & streams. Solve 40 Qs.",
    reasoningTopic: "Order & Ranking: Position from left/right, overlapping cases, finding total persons in row, maximum & minimum. Solve 35 Qs.",
    englishTopic: "Vocabulary: 100 high-frequency Idioms & Phrases with Hindi/English meanings + Error spotting drill (25 Qs).",
    gaTopic: "Geography: Solar System, Earth layers, Rivers of India (Himalayan vs Peninsular systems, tributaries), Mountain passes.",
    mockTestName: "Full Mock Test #6 (Test Speed on Arithmetic)"
  },
  {
    dayNumber: 7,
    phase: 1,
    title: "Averages, Mixtures, Non-Verbal Reasoning & Economy",
    quantTopic: "Averages & Alligation: Weighted averages, batsman/bowler average, replacement rules, Rule of Alligation in mixtures. Solve 35 Qs.",
    reasoningTopic: "Non-Verbal: Mirror & Water images, Paper folding and cutting, Figure completion. Solve 45 Qs.",
    englishTopic: "Vocabulary: 100 most frequent One-Word Substitutions (CGL PYQs) + 1 Reading Comprehension passage drill.",
    gaTopic: "Indian Economy: GDP, GNP, NNP, Inflation types (CPI, WPI), Repo/Reverse Repo rates, Monetary & Fiscal policies.",
    mockTestName: "Full Mock Test #7 (Focus on Reasoning Accuracy)"
  },
  {
    dayNumber: 8,
    phase: 1,
    title: "Number System, Mathematical Operations & Physics",
    quantTopic: "Number System: Unit digit theorem, divisibility rules (7, 11, 13, 72, 88), remainder theorem, number of zeroes. Solve 40 Qs.",
    reasoningTopic: "Mathematical Operations: Sign interchanging, BODMAS rules in reasoning equations, balancing equations. Solve 40 Qs.",
    englishTopic: "Sentence Improvement: 30 PYQ questions focusing on modifiers, parallelism, and redundant expressions.",
    gaTopic: "General Science (Physics): SI Units, Newton's laws of motion, Work-Energy-Power, Optics (Reflection, Refraction, Lenses).",
    mockTestName: "Full Mock Test #8 (Check Divisibility & Number Speed)"
  },
  {
    dayNumber: 9,
    phase: 1,
    title: "Algebra Identities, Cubes & Dice & Chemistry",
    quantTopic: "Algebra: Core algebraic identities (a+b)^2, a^3+b^3+c^3-3abc, value putting methods, symmetry concepts. Solve 35 Qs.",
    reasoningTopic: "Cubes & Dice: Standard vs Ordinary dice, opposite face determination, open dice folding patterns. Solve 40 Qs.",
    englishTopic: "Para Jumbles: Sentence rearrangement strategy (identifying opening sentences, mandatory pairs, pronoun references).",
    gaTopic: "General Science (Chemistry): Periodic table trends, Acids-Bases-Salts (pH values), Important chemical formulas & ores.",
    mockTestName: "Full Mock Test #9 (Advance Maths Introduction)"
  },
  {
    dayNumber: 10,
    phase: 1,
    title: "Geometry Foundations, Statement-Assumptions & Biology",
    quantTopic: "Geometry: Triangles (congruence, similarity, centers: orthocenter, circumcenter, incenter, centroid). Solve 35 Qs.",
    reasoningTopic: "Critical Reasoning: Statement & Assumptions, Statement & Conclusions, Course of Action basics. Solve 30 Qs.",
    englishTopic: "Spelling & Confusing Words: 50 frequently misspelled words in SSC exams + 2 Cloze Tests.",
    gaTopic: "General Science (Biology): Human digestive, circulatory, nervous systems; Vitamins & deficiency diseases; Plant cell vs animal cell.",
    mockTestName: "Full Mock Test #10 (Phase 1 Benchmark Evaluation)"
  },

  // Phase 2: Advance Speed Drills & Weak Link Elimination (Days 11 - 20)
  {
    dayNumber: 11,
    phase: 2,
    title: "Trigonometry Basics, Classification & Static GK Art/Culture",
    quantTopic: "Trigonometry: Basic ratios, complementary angles (sin(90-θ)=cosθ), value table (0, 30, 45, 60, 90), basic identities. Solve 35 Qs.",
    reasoningTopic: "Classification & Odd One Out: Word, alphabet, and number odd-one-out speed sets. Solve 40 Qs.",
    englishTopic: "Error Spotting Marathon: 40 questions covering adjectives, adverbs, and degrees of comparison.",
    gaTopic: "Static GK: Classical Dances (8 forms & renowned exponents), Folk Dances state-wise, Major cultural festivals of India.",
    mockTestName: "Full Mock Test #11 (Target 130+ Marks Milestone)"
  },
  {
    dayNumber: 12,
    phase: 2,
    title: "Mensuration 2D, Figure Counting & Ecology/Sanctuaries",
    quantTopic: "Mensuration 2D: Triangles, rectangles, rhombus, trapezium, circles, semicircles, inscribed/circumscribed shapes. Solve 35 Qs.",
    reasoningTopic: "Figure Counting: Counting triangles, squares, rectangles, straight lines in complex geometric figures. Solve 35 Qs.",
    englishTopic: "Synonyms & Antonyms: 80 high-yield exam words with contextual example sentences.",
    gaTopic: "Environment & Ecology: National Parks, Wildlife Sanctuaries, Tiger Reserves, Ramsar wetland sites in India.",
    mockTestName: "Full Mock Test #12 (Strict 60-Minute CBT Simulation)"
  },
  {
    dayNumber: 13,
    phase: 2,
    title: "Mensuration 3D, Matrix & Static GK Superlatives",
    quantTopic: "Mensuration 3D: Cube, cuboid, cylinder, cone, sphere, hemisphere, frustum, prism & pyramid formulas. Solve 35 Qs.",
    reasoningTopic: "Matrix & Pattern Recognition: Grid puzzles, missing character in matrices and circles. Solve 40 Qs.",
    englishTopic: "Fill in the Blanks: Single and double blank PYQs testing vocabulary nuance and phrasal verbs.",
    gaTopic: "Static GK: First in India (Male/Female), First in World, Longest/Highest/Largest geographical records.",
    mockTestName: "Full Mock Test #13 (Identify Advance Math Blindspots)"
  },
  {
    dayNumber: 14,
    phase: 2,
    title: "Data Interpretation, Advanced Syllogisms & International Bodies",
    quantTopic: "Data Interpretation: Tables, Bar charts, Pie charts, Line graphs; fast calculation of % change and ratio. Solve 25 Qs.",
    reasoningTopic: "Advanced Syllogisms: 'Only a few', 'Only', 'Can never be' special statements and multi-conclusion verification. Solve 35 Qs.",
    englishTopic: "Reading Comprehension: 2 lengthy editorial passages (speed reading, tone identification, central theme).",
    gaTopic: "International Organizations: UN bodies, World Bank, IMF, WTO, BRICS, SCO, ASEAN (Headquarters & Chiefs).",
    mockTestName: "Full Mock Test #14 (Speed Test on DI & Data Sets)"
  },
  {
    dayNumber: 15,
    phase: 2,
    title: "Height & Distance, Coordinate Geometry & Awards/Honors",
    quantTopic: "Height & Distance + Coordinate: 30-60-90 & 45-45-90 standard ratios, distance formula, slope, equation of line. Solve 35 Qs.",
    reasoningTopic: "Mixed Reasoning Speed Sprint: 25 questions in 14 minutes strictly timed.",
    englishTopic: "Phrasal Verbs Marathon: 'Look into', 'Bring up', 'Put off', 'Give in' etc. with 30 practice questions.",
    gaTopic: "Awards & Honors: Nobel Prizes, Bharat Ratna, Padma Awards, Sahitya Akademi, Oscar & Filmfare awards summary.",
    mockTestName: "Full Mock Test #15 (Mid-Way Performance Audit)"
  },
  {
    dayNumber: 16,
    phase: 2,
    title: "Arithmetic PYQ Sprint, Venn Diagrams & Census 2011",
    quantTopic: "Arithmetic PYQ Sprint: Top 40 hardest percentage, profit & time-work questions from CGL 2022-2024 Tier 1/2.",
    reasoningTopic: "Venn Diagrams: Geometric Venn representations, finding distinct category intersections. Solve 35 Qs.",
    englishTopic: "Cloze Test Mastery: 4 sets of 5-question cloze tests with vocabulary elimination strategies.",
    gaTopic: "Census 2011: Literacy rates, sex ratio, population density, top/bottom states + Major central government schemes.",
    mockTestName: "Full Mock Test #16 (Benchmark: Aim for 140+ Marks)"
  },
  {
    dayNumber: 17,
    phase: 2,
    title: "Advance Algebra & Polynomials, Embedded Figures & Sports",
    quantTopic: "Algebra Advance: Polynomial roots, maximum/minimum value of quadratic expressions, reciprocal equations. Solve 35 Qs.",
    reasoningTopic: "Embedded Figures & Pattern Completion: Identifying hidden patterns in complex backgrounds. Solve 40 Qs.",
    englishTopic: "Active/Passive & Direct/Indirect Lightning Test: 30 questions in 15 minutes.",
    gaTopic: "Sports & Games: Terminology, number of players, trophies, Olympic/Asian Games/Commonwealth medals tally.",
    mockTestName: "Full Mock Test #17 (Focus on Fast Grammar Elimination)"
  },
  {
    dayNumber: 18,
    phase: 2,
    title: "Geometry Circles & Tangents, Clock/Calendar & Space Tech",
    quantTopic: "Geometry Circles: Alternate segment theorem, tangent-secant theorem (PA*PB=PT^2), common tangents. Solve 35 Qs.",
    reasoningTopic: "Clock & Calendar: Angle between hands, reflex angles, leap years, finding day for any historical date. Solve 35 Qs.",
    englishTopic: "Spotting Errors PYQs: 40 questions from recent CGL exams with detailed rule notes.",
    gaTopic: "Science & Tech: ISRO space missions (Chandrayaan, Aditya-L1, Gaganyaan), DRDO missile systems & satellites.",
    mockTestName: "Full Mock Test #18 (Tackle Advance Geometry)"
  },
  {
    dayNumber: 19,
    phase: 2,
    title: "Trigonometry Maxima/Minima, Logical Deduction & Current Affairs Part 1",
    quantTopic: "Trigonometric Identities: Max/min values (a sinθ + b cosθ), elimination of angles, compound angles. Solve 35 Qs.",
    reasoningTopic: "Logical Sequence of Words & Dictionary Order: Speed arrangement of words. Solve 35 Qs.",
    englishTopic: "One Word Substitution & Idioms Rapid Fire: 150 combined revision flashcards.",
    gaTopic: "Current Affairs Revision Part 1: National news, state schemes, MoUs, appointments of previous 3 months.",
    mockTestName: "Full Mock Test #19 (Target Sub-50 Minute Total Time)"
  },
  {
    dayNumber: 20,
    phase: 2,
    title: "Mensuration & DI Hybrid, Full Reasoning Sprint & Current Affairs Part 2",
    quantTopic: "Mensuration & Geometry Combined Drills: Solve 35 multi-concept questions with short tricks.",
    reasoningTopic: "Full Reasoning 25-Question Speed Drill: Target completion in 12 minutes with 100% accuracy.",
    englishTopic: "Grammar Comprehensive Test: 50 mixed error spotting and sentence improvement questions.",
    gaTopic: "Current Affairs Revision Part 2: International news, indices & rankings (Happiness, Press Freedom, HDI), summits.",
    mockTestName: "Full Mock Test #20 (Phase 2 Climax - Full Review)"
  },

  // Phase 3: Real Exam Simulation, Rapid Revision & Tier-1 Readiness (Days 21 - 31)
  {
    dayNumber: 21,
    phase: 3,
    title: "Top 50 Tough Arithmetic PYQ Sprint & Complete Polity Review",
    quantTopic: "Top 50 Tough Arithmetic Questions: Time & Distance, CI/SI installments, and Mixture alligations.",
    reasoningTopic: "Reasoning Speed Test: 25 questions in 11 minutes (focus on skipping time-trap questions).",
    englishTopic: "English Speed Test: 25 questions in 9 minutes (rapid scanning & elimination).",
    gaTopic: "Polity Rapid Fire: Important Articles (1 to 395 key list), Schedules 1 to 12, Emergency provisions.",
    mockTestName: "Full Mock Test #21 (Phase 3 Full CBT Marathon)"
  },
  {
    dayNumber: 22,
    phase: 3,
    title: "Advance Maths Speed Sprint & Freedom Struggle Revision",
    quantTopic: "Advance Maths Sprint: Geometry, Mensuration, Algebra & Trigonometry high-yield formulas test.",
    reasoningTopic: "Non-Verbal & Puzzles Speed Drill: 35 questions in 15 minutes.",
    englishTopic: "Complete 100 Golden Rules of Grammar Quick Review (Part 1).",
    gaTopic: "Modern History Rapid Fire: Timeline from 1905 (Partition of Bengal) to 1947 (Independence), Gandhi's movements.",
    mockTestName: "Full Mock Test #22 (Focus on Cut-off Clearance Strategy)"
  },
  {
    dayNumber: 23,
    phase: 3,
    title: "Calculation Tricks, Logic Drills & Geography Quick Scan",
    quantTopic: "Quick Calculation Shortcuts: Vedic math splits, digital sum method, approximations for DI.",
    reasoningTopic: "Analogy, Series & Coding Final Review: Review tricky patterns from error notebook.",
    englishTopic: "Complete 100 Golden Rules of Grammar Quick Review (Part 2) + 20 Phrasal Verbs.",
    gaTopic: "Geography Rapid Fire: Major Minerals, Dams & Reservoirs, Soil types, Biosphere reserves.",
    mockTestName: "Full Mock Test #23 (Test Pressure Management)"
  },
  {
    dayNumber: 24,
    phase: 3,
    title: "CGL 2024 Tier-1 Shift 1 Paper Emulation",
    quantTopic: "Solve official CGL 2024 Shift 1 Quant section in 20 minutes; analyze alternate shorter methods.",
    reasoningTopic: "Solve official CGL 2024 Shift 1 Reasoning section in 12 minutes.",
    englishTopic: "Solve official CGL 2024 Shift 1 English section in 10 minutes.",
    gaTopic: "Solve official CGL 2024 Shift 1 GA section in 6 minutes; revise all related static concepts.",
    mockTestName: "Full Mock Test #24 (Official PYQ Real Paper Simulation)"
  },
  {
    dayNumber: 25,
    phase: 3,
    title: "CGL 2024 Tier-1 Shift 2 Paper Emulation",
    quantTopic: "Solve official CGL 2024 Shift 2 Quant section in 20 minutes; identify time traps.",
    reasoningTopic: "Solve official CGL 2024 Shift 2 Reasoning section in 12 minutes.",
    englishTopic: "Solve official CGL 2024 Shift 2 English section in 10 minutes.",
    gaTopic: "Solve official CGL 2024 Shift 2 GA section in 6 minutes; review static GK topics asked.",
    mockTestName: "Full Mock Test #25 (Official PYQ Paper Simulation)"
  },
  {
    dayNumber: 26,
    phase: 3,
    title: "Formula Flashcards, Error Notebook Review & Static GK Marathon",
    quantTopic: "Quant Formula Notebook: Revise all 150+ formulas and shortcuts without solving new questions.",
    reasoningTopic: "Review all missed questions from Mocks 1-25 in the personal Error Notebook.",
    englishTopic: "Revise all 300+ bookmarked Vocab, Idioms & One-word substitutions.",
    gaTopic: "Static GK Marathon: Musical instruments & players, Temple architecture, Books & authors.",
    mockTestName: "Full Mock Test #26 (Target Personal Best Score)"
  },
  {
    dayNumber: 27,
    phase: 3,
    title: "Sectional Speed Master Class & Economy/Budget Scan",
    quantTopic: "Quant Speed Marathon: 25 arithmetic + 25 advance questions in two separate 20-min blocks.",
    reasoningTopic: "Reasoning Sprint: 2 full 25-question sectional mocks back to back.",
    englishTopic: "English Sprint: 2 full 25-question sectional mocks back to back.",
    gaTopic: "Economic Survey, Union Budget Highlights & GST basics quick scan.",
    mockTestName: "Full Mock Test #27 (Exam Timing Synchronization)"
  },
  {
    dayNumber: 28,
    phase: 3,
    title: "Confidence Booster Mock & Comprehensive Science Scan",
    quantTopic: "Review top 30 favorite question types in Quant; practice mental calculation.",
    reasoningTopic: "Light reasoning practice: Blood relations, Direction, Matrix.",
    englishTopic: "Rapid scan through Confusing Words & Spelling rules.",
    gaTopic: "Complete General Science Quick Scan (Physics units, Chemistry formulas, Biology vitamins).",
    mockTestName: "Full Mock Test #28 (High Confidence Test Series Simulation)"
  },
  {
    dayNumber: 29,
    phase: 3,
    title: "Mindset Calibration, Light Drills & Error Book Clean-up",
    quantTopic: "Light revision of arithmetic formulas; zero stress, no new concepts.",
    reasoningTopic: "Relaxed pattern scanning; 15 easy questions to maintain rhythm.",
    englishTopic: "Read 2 high-quality editorial articles to keep English reading comprehension fluid.",
    gaTopic: "Last 6 Months top 100 Current Affairs MCQs rapid glance.",
    mockTestName: "Full Mock Test #29 (Final Strict Full-Length Mock)"
  },
  {
    dayNumber: 30,
    phase: 3,
    title: "Final Day Formula Mastery & Exam Day Readiness",
    quantTopic: "Formula sheet read-through (Geometry theorems, Trigonometric table, Mensuration formulas).",
    reasoningTopic: "Mental relaxation; review test attempt strategy (Order: Reasoning -> GA -> English -> Quant).",
    englishTopic: "Light scan of golden grammar rules; positive affirmation.",
    gaTopic: "Static GK one-liners flip-through; keep mind fresh.",
    mockTestName: "Full Mock Test #30 (Final Confidence Booster & Exam Simulation)"
  },
  {
    dayNumber: 31,
    phase: 3,
    title: "Grand Exam Eve & Tier-1 Exam Readiness",
    quantTopic: "Final formula handbook review: Geometry direct theorems, Mensuration 2D/3D, Algebra identities. Keep stress low.",
    reasoningTopic: "Quick glance at tricky alphabet coding and non-verbal pattern traps. Relaxed pacing.",
    englishTopic: "Last review of top 100 idioms, frequently confused words, and active voice rules.",
    gaTopic: "Review last 6 months high-priority current affairs list and important constitutional articles.",
    mockTestName: "Full Mock Test #31 (Grand Final Rehearsal & Score Lock-In)"
  }
];

// 13 Daily Slots (10 AM to 10 PM)
const DAILY_SLOTS = [
  {
    id: "slot_quant",
    type: "STUDY_QUANT",
    category: "study",
    startTime: "10:00 AM",
    endTime: "11:30 AM",
    durationMinutes: 90,
    title: "Quant Mastery & Problem Solving",
    subtitle: "Core arithmetic & advance math problem sets, speed techniques",
    icon: "📐",
    isBreak: false,
    defaultSubtasks: [
      "Revise core formulas & shortcut splits",
      "Solve 35-40 targeted problem questions",
      "Record tricky traps in Error Book"
    ]
  },
  {
    id: "slot_break1",
    type: "BREAK",
    category: "break",
    startTime: "11:30 AM",
    endTime: "11:45 AM",
    durationMinutes: 15,
    title: "Hydration & Stretch Break",
    subtitle: "Step away from desk, eye relaxation & deep breathing",
    icon: "☕",
    isBreak: true,
    defaultSubtasks: ["Hydrate & gentle spine stretch", "Rest eyes away from screen"]
  },
  {
    id: "slot_reasoning",
    type: "STUDY_REASONING",
    category: "study",
    startTime: "11:45 AM",
    endTime: "01:15 PM",
    durationMinutes: 90,
    title: "Reasoning Speed Drills",
    subtitle: "Logical puzzles, series, syllogisms & non-verbal figures",
    icon: "🧩",
    isBreak: false,
    defaultSubtasks: [
      "Review reverse pairs & square/cube series",
      "Solve 35-40 timed reasoning questions",
      "Verify Venn diagram possibilities"
    ]
  },
  {
    id: "slot_break2",
    type: "BREAK",
    category: "break",
    startTime: "01:15 PM",
    endTime: "01:30 PM",
    durationMinutes: 15,
    title: "Pre-Lunch Wind-down",
    subtitle: "Organize morning notes & prepare for afternoon mock",
    icon: "☕",
    isBreak: true,
    defaultSubtasks: ["Review morning study progress", "Set up CBT mock interface"]
  },
  {
    id: "slot_lunch",
    type: "LUNCH",
    category: "meal",
    startTime: "01:30 PM",
    endTime: "02:30 PM",
    durationMinutes: 60,
    title: "Lunch Break (1 Hour)",
    subtitle: "Nutritious meal & rest to recharge brain energy",
    icon: "🍱",
    isBreak: true,
    defaultSubtasks: ["Nutritious meal", "Power nap / relaxation (20 min)"]
  },
  {
    id: "slot_mock",
    type: "MOCK_TEST",
    category: "mock",
    startTime: "02:30 PM",
    endTime: "03:30 PM",
    durationMinutes: 60,
    title: "Full-Length Mock Test (CBT Mode)",
    subtitle: "Strict 60-min timer, 100 questions (200 marks) simulation",
    icon: "📝",
    isBreak: false,
    defaultSubtasks: [
      "Attempt Reasoning (12 min)",
      "Attempt GA & CA (6 min)",
      "Attempt English (10 min)",
      "Attempt Quant (25 min)",
      "Buffer review & mark check (7 min)"
    ]
  },
  {
    id: "slot_mock_analysis",
    type: "MOCK_ANALYSIS",
    category: "mock",
    startTime: "03:30 PM",
    endTime: "04:30 PM",
    durationMinutes: 60,
    title: "In-Depth Mock Analysis & Error Book",
    subtitle: "Categorize silly mistakes, unattempted Qs & solution methods",
    icon: "🔍",
    isBreak: false,
    defaultSubtasks: [
      "Analyze all negative marks",
      "Check unattempted easy questions",
      "Log scores into Mock Tracker"
    ]
  },
  {
    id: "slot_break3",
    type: "BREAK",
    category: "break",
    startTime: "04:30 PM",
    endTime: "04:45 PM",
    durationMinutes: 15,
    title: "Afternoon Tea / Coffee Break",
    subtitle: "Quick mental reset before language session",
    icon: "☕",
    isBreak: true,
    defaultSubtasks: ["Tea or coffee recharge", "Light breathing & fresh air"]
  },
  {
    id: "slot_english",
    type: "STUDY_ENGLISH",
    category: "study",
    startTime: "04:45 PM",
    endTime: "05:45 PM",
    durationMinutes: 60,
    title: "English Language & Comprehension",
    subtitle: "Grammar rules, Cloze test, Idioms, OWS & reading practice",
    icon: "📖",
    isBreak: false,
    defaultSubtasks: [
      "Revise 10 Golden Grammar Rules",
      "Solve 2 Cloze Tests + 1 RC Passage",
      "Revise 30 Idioms & 30 OWS"
    ]
  },
  {
    id: "slot_walking",
    type: "WALKING",
    category: "walk",
    startTime: "05:45 PM",
    endTime: "06:45 PM",
    durationMinutes: 60,
    title: "Evening Walking (1 Hour)",
    subtitle: "Brisk outdoor walking for blood circulation & stamina",
    icon: "🚶",
    isBreak: true,
    defaultSubtasks: ["4,000 to 6,000 brisk steps", "Mental audio recall of formulas"]
  },
  {
    id: "slot_ga",
    type: "STUDY_GA",
    category: "study",
    startTime: "06:45 PM",
    endTime: "08:00 PM",
    durationMinutes: 75,
    title: "General Awareness & Current Affairs",
    subtitle: "Polity, History, Geography, Science & Monthly CA quizzes",
    icon: "🌍",
    isBreak: false,
    defaultSubtasks: [
      "Revise high-yield static GK notes",
      "Attempt 50 Current Affairs MCQs",
      "Review Constitution Articles / Dances"
    ]
  },
  {
    id: "slot_dinner",
    type: "DINNER",
    category: "meal",
    startTime: "08:00 PM",
    endTime: "09:00 PM",
    durationMinutes: 60,
    title: "Dinner Break (1 Hour)",
    subtitle: "Relaxed meal & family/unwind time",
    icon: "🍽️",
    isBreak: true,
    defaultSubtasks: ["Healthy dinner", "Relaxation & unwind"]
  },
  {
    id: "slot_revision",
    type: "REVISION",
    category: "revision",
    startTime: "09:00 PM",
    endTime: "10:00 PM",
    durationMinutes: 60,
    title: "Rigorous Nightly Revision & Formula Book",
    subtitle: "Review all math formulas, today's mistakes & vocab flashcards",
    icon: "✨",
    isBreak: false,
    defaultSubtasks: [
      "Scan entire Quant formula notebook",
      "Review today's Error Book entries",
      "Lock in daily syllabus checklist"
    ]
  }
];

// Initial Formula Flashcards
const DEFAULT_FORMULA_CARDS = [
  {
    title: "Percentage to Fraction Equivalents",
    subject: "Quantitative Aptitude",
    category: "Arithmetic",
    content: "1/2 = 50%, 1/3 = 33.33%, 1/4 = 25%, 1/5 = 20%, 1/6 = 16.67%, 1/7 = 14.28%, 1/8 = 12.5%, 1/9 = 11.11%, 1/11 = 9.09%, 1/12 = 8.33%, 1/14 = 7.14%, 1/16 = 6.25%",
    rule: "% Change = (Change / Original) × 100"
  },
  {
    title: "Successive Percentage & Discounts",
    subject: "Quantitative Aptitude",
    category: "Arithmetic",
    content: "Two successive changes a% and b% result in net change: a + b + (ab/100)%. For discounts, net discount = d1 + d2 - (d1 × d2 / 100)%.",
    rule: "Net % = a + b + (ab/100)%"
  },
  {
    title: "Simple & Compound Interest Differences",
    subject: "Quantitative Aptitude",
    category: "Arithmetic",
    content: "Difference between CI and SI for 2 years: D2 = P × (R/100)². For 3 years: D3 = P × (R/100)² × (3 + R/100).",
    rule: "D2 = P(R/100)², D3 = P(R/100)²(3 + R/100)"
  },
  {
    title: "Algebra Key Identities",
    subject: "Quantitative Aptitude",
    category: "Advance Maths",
    content: "a³ + b³ + c³ - 3abc = (a+b+c)(a² + b² + c² - ab - bc - ca). If a + b + c = 0, then a³ + b³ + c³ = 3abc.",
    rule: "If x + 1/x = k, then x² + 1/x² = k² - 2; x³ + 1/x³ = k³ - 3k"
  },
  {
    title: "Geometry: Circles & Tangents Theorems",
    subject: "Quantitative Aptitude",
    category: "Advance Maths",
    content: "1. Tangent-Secant theorem: PT² = PA × PB. 2. Two chords intersecting at P: PA × PB = PC × PD. 3. Alternate segment theorem: Angle between tangent and chord equals opposite segment angle.",
    rule: "Direct Common Tangent = √[d² - (r1 - r2)²]; Transverse = √[d² - (r1 + r2)²]"
  },
  {
    title: "Alphabet Positions & Reverse Pairs",
    subject: "Reasoning",
    category: "Coding-Decoding",
    content: "EJOTY (5, 10, 15, 20, 25). Reverse Pairs (Sum = 27): A-Z (Azad), B-Y (Boy), C-X (Crux), D-W (Dew), E-V (Evening), F-U (Fun), G-T (GT Road), H-S (High School), I-R (Indian Railway), J-Q (Jungle Queen), K-P (Kanpur), L-O (Love), M-N (Man).",
    rule: "Reverse position = 27 - Forward position"
  },
  {
    title: "Clock Angles & Calendar Rules",
    subject: "Reasoning",
    category: "Analytical",
    content: "Angle between hands = |30H - (11/2)M|. Hands coincide 11 times in 12 hours (22 times in 24 hrs). Normal year has 1 odd day; leap year has 2 odd days.",
    rule: "Angle = |30H - 5.5M|"
  },
  {
    title: "Syllogism Standard Deduction Rules",
    subject: "Reasoning",
    category: "Verbal Logic",
    content: "All + All = All. All + No = No. Some + All = Some. Some + No = Some-Not. 'Only a few A are B' means: Some A are B AND Some A are NOT B.",
    rule: "Complementary pairs: (Some + No) or (All + Some Not)"
  },
  {
    title: "Subject-Verb Agreement Golden Rules",
    subject: "English",
    category: "Grammar",
    content: "1. With 'along with', 'as well as', 'together with', verb agrees with the first subject. 2. With 'neither..nor', 'either..or', verb agrees with nearest subject. 3. 'One of + Plural Noun + Singular Verb'. 4. 'The number of' = singular; 'A number of' = plural.",
    rule: "Subject governs verb number; ignore intervening phrases"
  },
  {
    title: "Conditional Sentences & Inversion",
    subject: "English",
    category: "Grammar",
    content: "1st: If + Simple Present, will + V1. 2nd: If + Simple Past, would + V1. 3rd: If + Past Perfect (had + V3), would have + V3. Inversion: 'No sooner had he...' followed by 'than'.",
    rule: "No sooner ... than; Hardly / Scarcely ... when"
  },
  {
    title: "Indian Constitution: Key Articles",
    subject: "General Awareness",
    category: "Indian Polity",
    content: "Art 14 (Equality), Art 17 (Untouchability abolition), Art 19 (6 Freedoms), Art 21 (Life & Liberty), Art 21A (Education), Art 32 (Constitutional Remedies), Art 44 (UCC), Art 51A (Duties), Art 110 (Money Bill), Art 123 (Ordinance), Art 324 (Election Commission), Art 368 (Amendment).",
    rule: "Emergencies: 352 (National), 356 (State), 360 (Financial)"
  },
  {
    title: "Human Body & Vitamins Deficiency",
    subject: "General Awareness",
    category: "General Science",
    content: "Vitamin A (Retinol) - Night blindness; B1 (Thiamine) - Beriberi; B3 (Niacin) - Pellagra; B12 - Pernicious anaemia; C (Ascorbic acid) - Scurvy; D (Calciferol) - Rickets; K (Phylloquinone) - Failure in blood clotting.",
    rule: "Fat Soluble: K-E-D-A; Water Soluble: B & C"
  }
];

// Helper to generate 124 pre-populated topics from 31 days
function generateInitialTopics() {
  const topics = [];
  let idCounter = 1;
  DEFAULT_DAY_PLANS.forEach(day => {
    // Quant
    topics.push({
      id: `topic_${idCounter++}`,
      dayNumber: day.dayNumber,
      subject: "Quantitative Aptitude",
      title: day.quantTopic.split(". Solve")[0].split(". Practice")[0].trim(),
      status: "Not Started",
      notes: ""
    });
    // Reasoning
    topics.push({
      id: `topic_${idCounter++}`,
      dayNumber: day.dayNumber,
      subject: "Reasoning",
      title: day.reasoningTopic.split(". Solve")[0].split(". Practice")[0].trim(),
      status: "Not Started",
      notes: ""
    });
    // English
    topics.push({
      id: `topic_${idCounter++}`,
      dayNumber: day.dayNumber,
      subject: "English",
      title: day.englishTopic.split(". Solve")[0].split(". Practice")[0].trim(),
      status: "Not Started",
      notes: ""
    });
    // GA
    topics.push({
      id: `topic_${idCounter++}`,
      dayNumber: day.dayNumber,
      subject: "General Awareness",
      title: day.gaTopic.split(". Solve")[0].split(". Practice")[0].trim(),
      status: "Not Started",
      notes: ""
    });
  });
  return topics;
}

// Helper to generate 31 mock test slots
function generateInitialMockTests() {
  const providers = ["Testbook", "Oliveboard", "PYQ 2024 Shift", "SuperCoaching", "Adda247"];
  const list = [];
  for (let i = 1; i <= 31; i++) {
    const provider = providers[(i - 1) % providers.length];
    list.push({
      testNumber: i,
      title: `SSC CGL Tier-1 Full Mock #${i}`,
      provider: provider,
      isAttempted: false,
      quantScore: 0,
      reasoningScore: 0,
      englishScore: 0,
      gaScore: 0,
      totalScore: 0,
      attemptedQuestions: 0,
      accuracyPercent: 0,
      timeTakenMinutes: 60,
      weakTopics: "",
      errorAnalysis: ""
    });
  }
  return list;
}

// 2. STATE STORE & LOCALSTORAGE PERSISTENCE
class AppStore {
  constructor() {
    this.STORAGE_KEY = "focuscgl_v1_store";
    this.state = this.loadState();
  }

  loadState() {
    try {
      const data = localStorage.getItem(this.STORAGE_KEY);
      if (data) {
        return JSON.parse(data);
      }
    } catch (e) {
      console.error("Failed to parse stored state:", e);
    }

    // Default Fresh State
    return {
      selectedDay: 1,
      // Map of dayNumber -> { completedBlockIds: string[], customSubtasks: Record<slotId, Array<{id, text, done}>>, notes: string }
      daysData: {},
      topics: generateInitialTopics(),
      mockTests: generateInitialMockTests(),
      pomodoroStats: {
        completedCycles: 0,
        focusMinutes: 0
      },
      theme: "dark"
    };
  }

  saveState() {
    try {
      localStorage.setItem(this.STORAGE_KEY, JSON.stringify(this.state));
    } catch (e) {
      console.error("Failed to save state to localStorage:", e);
    }
  }

  resetAllData() {
    localStorage.removeItem(this.STORAGE_KEY);
    this.state = this.loadState();
    this.saveState();
  }

  getDayData(dayNumber) {
    if (!this.state.daysData[dayNumber]) {
      this.state.daysData[dayNumber] = {
        completedBlockIds: [],
        customSubtasks: {},
        notes: ""
      };
    }
    return this.state.daysData[dayNumber];
  }

  toggleBlock(dayNumber, slotId) {
    const day = this.getDayData(dayNumber);
    const idx = day.completedBlockIds.indexOf(slotId);
    if (idx >= 0) {
      day.completedBlockIds.splice(idx, 1);
    } else {
      day.completedBlockIds.push(slotId);
    }
    this.saveState();
  }

  setAllDayBlocks(dayNumber, complete) {
    const day = this.getDayData(dayNumber);
    if (complete) {
      day.completedBlockIds = DAILY_SLOTS.filter(s => !s.isBreak).map(s => s.id);
    } else {
      day.completedBlockIds = [];
    }
    this.saveState();
  }

  saveDayNotes(dayNumber, notes) {
    const day = this.getDayData(dayNumber);
    day.notes = notes;
    this.saveState();
  }

  toggleSubtask(dayNumber, slotId, subtaskId) {
    const day = this.getDayData(dayNumber);
    if (!day.customSubtasks[slotId]) return;
    const sub = day.customSubtasks[slotId].find(s => s.id === subtaskId);
    if (sub) {
      sub.done = !sub.done;
      this.saveState();
    }
  }

  addSubtask(dayNumber, slotId, text) {
    const day = this.getDayData(dayNumber);
    if (!day.customSubtasks[slotId]) {
      day.customSubtasks[slotId] = [];
    }
    day.customSubtasks[slotId].push({
      id: "sub_" + Date.now(),
      text: text,
      done: false
    });
    this.saveState();
  }

  updateTopicStatus(topicId, newStatus) {
    const topic = this.state.topics.find(t => t.id === topicId);
    if (topic) {
      topic.status = newStatus;
      this.saveState();
    }
  }

  updateTopicNotes(topicId, notes) {
    const topic = this.state.topics.find(t => t.id === topicId);
    if (topic) {
      topic.notes = notes;
      this.saveState();
    }
  }

  addCustomTopic(dayNumber, subject, title, status, notes) {
    const newTopic = {
      id: "topic_custom_" + Date.now(),
      dayNumber: parseInt(dayNumber, 10),
      subject: subject,
      title: title,
      status: status,
      notes: notes
    };
    this.state.topics.push(newTopic);
    this.saveState();
    return newTopic;
  }

  saveMockScore(testNumber, updateData) {
    const mock = this.state.mockTests.find(m => m.testNumber === testNumber);
    if (mock) {
      Object.assign(mock, updateData);
      mock.isAttempted = true;
      this.saveState();
    }
  }

  incrementPomodoroCycle(durationMinutes) {
    this.state.pomodoroStats.completedCycles += 1;
    this.state.pomodoroStats.focusMinutes += durationMinutes;
    this.saveState();
  }
}

// 3. SOUND SYNTHESIS VIA WEB AUDIO API (Clean chime, no external files)
class AudioService {
  constructor() {
    this.ctx = null;
  }

  init() {
    if (!this.ctx) {
      const AudioContext = window.AudioContext || window.webkitAudioContext;
      if (AudioContext) {
        this.ctx = new AudioContext();
      }
    }
  }

  playCompletionChime() {
    try {
      this.init();
      if (!this.ctx) return;
      if (this.ctx.state === "suspended") {
        this.ctx.resume();
      }

      const now = this.ctx.currentTime;
      // Synthesize elegant 3-tone gong bell (F5 -> A5 -> C6)
      const freqs = [698.46, 880.00, 1046.50];
      freqs.forEach((freq, i) => {
        const osc = this.ctx.createOscillator();
        const gain = this.ctx.createGain();

        osc.type = "sine";
        osc.frequency.setValueAtTime(freq, now + i * 0.12);

        gain.gain.setValueAtTime(0, now + i * 0.12);
        gain.gain.linearRampToValueAtTime(0.3, now + i * 0.12 + 0.04);
        gain.gain.exponentialRampToValueAtTime(0.001, now + i * 0.12 + 1.2);

        osc.connect(gain);
        gain.connect(this.ctx.destination);

        osc.start(now + i * 0.12);
        osc.stop(now + i * 0.12 + 1.25);
      });
    } catch (e) {
      console.warn("Audio chime playback ignored:", e);
    }
  }
}

// 4. MAIN CONTROLLER
const store = new AppStore();
const audioService = new AudioService();

// Pomodoro Timer Runtime State
let timerInterval = null;
let timerRunning = false;
let timerSecondsRemaining = 25 * 60;
let timerTotalSeconds = 25 * 60;
let currentPomodoroMode = "POMODORO_25";
let activeSlotId = "slot_quant";

// DOM INITIALIZATION
document.addEventListener("DOMContentLoaded", () => {
  setupTheme();
  setupNavigation();
  setupDayCarousel();
  setupTimeline();
  setupPomodoroTimer();
  setupStreakTracker();
  setupTopicRevision();
  setupMockTests();
  setupFormulaCards();
  setupModals();

  renderActiveDay();
  updateStreakUI();
  updateTimelineIndicator();

  // Periodic timeline indicator check
  setInterval(updateTimelineIndicator, 60000);
});

// THEME HANDLING
function setupTheme() {
  const html = document.documentElement;
  const toggleBtn = document.getElementById("themeToggleBtn");
  const sunIcon = toggleBtn.querySelector(".sun-icon");
  const moonIcon = toggleBtn.querySelector(".moon-icon");

  function applyTheme(theme) {
    html.setAttribute("data-theme", theme);
    store.state.theme = theme;
    store.saveState();
    if (theme === "dark") {
      sunIcon.classList.remove("hidden");
      moonIcon.classList.add("hidden");
    } else {
      sunIcon.classList.add("hidden");
      moonIcon.classList.remove("hidden");
    }
  }

  applyTheme(store.state.theme || "dark");

  toggleBtn.addEventListener("click", () => {
    const current = html.getAttribute("data-theme") || "dark";
    applyTheme(current === "dark" ? "light" : "dark");
  });
}

// NAVIGATION TABS
function setupNavigation() {
  const tabs = document.querySelectorAll(".nav-tab, .mobile-nav-btn");
  const contents = document.querySelectorAll(".tab-content");

  function switchTab(tabId) {
    tabs.forEach(t => {
      if (t.dataset.tab === tabId) t.classList.add("active");
      else t.classList.remove("active");
    });
    contents.forEach(c => {
      if (c.id === `tab-${tabId}`) c.classList.add("active");
      else c.classList.remove("active");
    });

    if (tabId === "streak") updateStreakUI();
    if (tabId === "mocks") renderMockTests();
    if (tabId === "revision") renderTopicRevision();
  }

  tabs.forEach(tab => {
    tab.addEventListener("click", () => {
      switchTab(tab.dataset.tab);
    });
  });

  // Header quick pills
  document.getElementById("streakBadgeBtn").addEventListener("click", () => switchTab("streak"));
  document.getElementById("activeTimerBadgeBtn").addEventListener("click", () => switchTab("pomodoro"));
}

// DAY CAROUSEL SELECTOR
function setupDayCarousel() {
  const carousel = document.getElementById("dayCarousel");
  carousel.innerHTML = "";

  for (let i = 1; i <= 31; i++) {
    const chip = document.createElement("div");
    chip.className = `day-chip ${i === store.state.selectedDay ? "active" : ""}`;
    chip.dataset.day = i;

    const dayData = store.getDayData(i);
    const completedCount = dayData.completedBlockIds.length;
    if (completedCount >= 7) {
      chip.classList.add("completed");
    }

    chip.innerHTML = `
      <span class="day-chip-label">Day ${i}</span>
      <span class="day-chip-sub">${completedCount > 0 ? `${completedCount}/10` : "Ready"}</span>
    `;

    chip.addEventListener("click", () => {
      store.state.selectedDay = i;
      store.saveState();
      document.querySelectorAll(".day-chip").forEach(c => c.classList.remove("active"));
      chip.classList.add("active");
      renderActiveDay();
    });

    carousel.appendChild(chip);
  }
}

function updateDayCarouselBadges() {
  document.querySelectorAll(".day-chip").forEach(chip => {
    const day = parseInt(chip.dataset.day, 10);
    const dayData = store.getDayData(day);
    const count = dayData.completedBlockIds.length;
    const sub = chip.querySelector(".day-chip-sub");
    if (sub) {
      sub.textContent = count > 0 ? `${count}/10` : "Ready";
    }
    if (count >= 7) chip.classList.add("completed");
    else chip.classList.remove("completed");
  });
}

// RENDER ACTIVE DAY SCHEDULE
function renderActiveDay() {
  const dayNum = store.state.selectedDay;
  const dayPlan = DEFAULT_DAY_PLANS.find(p => p.dayNumber === dayNum) || DEFAULT_DAY_PLANS[0];
  const dayData = store.getDayData(dayNum);

  // Update Hero Card
  document.getElementById("currentDayPhaseBadge").textContent = `Phase ${dayPlan.phase}: ${
    dayPlan.phase === 1 ? "Core Foundations" : dayPlan.phase === 2 ? "Speed Drills" : "Real Exam Simulation"
  }`;
  document.getElementById("currentDayTitle").textContent = `Day ${dayPlan.dayNumber}: ${dayPlan.title}`;

  const academicBlocks = DAILY_SLOTS.filter(s => !s.isBreak);
  const totalAcademic = academicBlocks.length; // 10 study blocks
  const completedAcademic = dayData.completedBlockIds.filter(id => {
    const s = DAILY_SLOTS.find(slot => slot.id === id);
    return s && !s.isBreak;
  }).length;

  const pct = Math.round((completedAcademic / totalAcademic) * 100);
  document.getElementById("currentDayMeta").textContent = `${completedAcademic} of ${totalAcademic} blocks completed • ${pct}% Today's Progress`;
  document.getElementById("currentDayLinearProgress").style.width = `${pct}%`;

  const circleStroke = document.getElementById("currentDayCircleProgress");
  circleStroke.setAttribute("stroke-dasharray", `${pct}, 100`);
  document.getElementById("currentDayPercentageText").textContent = `${pct}%`;

  // Update Curriculum Targets
  document.getElementById("curriculumDayLabel").textContent = `Day ${dayNum}`;
  const grid = document.getElementById("curriculumGrid");
  grid.innerHTML = `
    <div class="curriculum-card-mini quant">
      <div class="mini-subject-title">📐 Quantitative Aptitude</div>
      <div class="mini-topic-desc">${dayPlan.quantTopic}</div>
    </div>
    <div class="curriculum-card-mini reasoning">
      <div class="mini-subject-title">🧩 Reasoning Speed</div>
      <div class="mini-topic-desc">${dayPlan.reasoningTopic}</div>
    </div>
    <div class="curriculum-card-mini english">
      <div class="mini-subject-title">📖 English Language</div>
      <div class="mini-topic-desc">${dayPlan.englishTopic}</div>
    </div>
    <div class="curriculum-card-mini ga">
      <div class="mini-subject-title">🌍 General Awareness</div>
      <div class="mini-topic-desc">${dayPlan.gaTopic}</div>
    </div>
    <div class="curriculum-card-mini mock">
      <div class="mini-subject-title">📝 Tier-1 Mock Simulation</div>
      <div class="mini-topic-desc">${dayPlan.mockTestName}</div>
    </div>
  `;

  // Render Slots List
  renderSlotsList(dayPlan, dayData);

  // Render Daily Subject Revisions Quick Section
  renderDailyTopicRevisionSection(dayNum);

  // Update Carousel chips
  updateDayCarouselBadges();
  updateStreakUI();
}

function renderSlotsList(dayPlan, dayData) {
  const container = document.getElementById("slotsList");
  container.innerHTML = "";

  DAILY_SLOTS.forEach(slot => {
    const isChecked = dayData.completedBlockIds.includes(slot.id);
    const isActiveTimer = activeSlotId === slot.id && timerRunning;

    // Daily custom syllabus text for this slot
    let slotSyllabus = slot.subtitle;
    if (slot.type === "STUDY_QUANT") slotSyllabus = dayPlan.quantTopic;
    else if (slot.type === "STUDY_REASONING") slotSyllabus = dayPlan.reasoningTopic;
    else if (slot.type === "STUDY_ENGLISH") slotSyllabus = dayPlan.englishTopic;
    else if (slot.type === "STUDY_GA") slotSyllabus = dayPlan.gaTopic;
    else if (slot.type === "MOCK_TEST") slotSyllabus = dayPlan.mockTestName;

    const card = document.createElement("div");
    card.className = `slot-card ${isChecked ? "completed" : ""} ${isActiveTimer ? "active-timer-slot" : ""}`;
    card.id = `card_${slot.id}`;

    // Sub-tasks for this slot
    const defaultSubs = slot.defaultSubtasks.map((txt, idx) => ({
      id: `def_${idx}`,
      text: txt,
      done: isChecked
    }));
    const userSubs = dayData.customSubtasks[slot.id] || [];
    const allSubs = [...defaultSubs, ...userSubs];

    card.innerHTML = `
      <div class="slot-header">
        <div class="slot-header-left">
          <div class="slot-icon-box">${slot.icon}</div>
          <div>
            <div class="slot-time-badge">${slot.startTime} – ${slot.endTime} • ${slot.durationMinutes}m</div>
            <div class="slot-title">${slot.title}</div>
          </div>
        </div>
        ${
          !slot.isBreak
            ? `<button class="slot-checkbox-btn ${isChecked ? "checked" : ""}" title="Toggle Block Completion">✓</button>`
            : ""
        }
      </div>

      <div class="slot-body">
        <p class="slot-desc">${slotSyllabus}</p>

        <!-- Sub-tasks Checklist -->
        <div class="subtasks-wrapper">
          <div class="subtasks-header">
            <span>Block Sub-Tasks (${allSubs.filter(s => s.done).length}/${allSubs.length})</span>
          </div>
          <div class="subtasks-list" id="subtasks_${slot.id}">
            ${allSubs
              .map(
                sub => `
              <label class="subtask-item ${sub.done ? "done" : ""}">
                <input type="checkbox" ${sub.done ? "checked" : ""} data-subid="${sub.id}">
                <span>${sub.text}</span>
              </label>
            `
              )
              .join("")}
          </div>
          <div class="add-subtask-row">
            <input type="text" class="add-subtask-input" placeholder="+ Add custom sub-task for this block..." id="input_sub_${slot.id}">
            <button class="btn btn-outline btn-sm add-sub-btn" data-slot="${slot.id}">Add</button>
          </div>
        </div>

        <!-- Quick Slot Actions -->
        <div class="slot-actions">
          <button class="btn btn-primary btn-sm slot-pomo-btn" data-slot="${slot.id}" data-mode="POMODORO_25">
            🍅 25m Focus
          </button>
          <button class="btn btn-outline btn-sm slot-full-btn" data-slot="${slot.id}" data-mode="FULL_BLOCK" data-duration="${slot.durationMinutes}">
            ⏱️ ${slot.durationMinutes}m Block
          </button>
          ${
            slot.type === "MOCK_TEST"
              ? `<button class="btn btn-secondary btn-sm slot-mock-btn">📊 Score Tracker</button>`
              : ""
          }
        </div>
      </div>
    `;

    // Toggle Block Completion
    const checkBtn = card.querySelector(".slot-checkbox-btn");
    if (checkBtn) {
      checkBtn.addEventListener("click", () => {
        store.toggleBlock(dayPlan.dayNumber, slot.id);
        renderActiveDay();
      });
    }

    // Toggle Subtasks
    card.querySelectorAll(".subtask-item input").forEach(chk => {
      chk.addEventListener("change", e => {
        const subId = chk.dataset.subid;
        if (subId.startsWith("def_")) {
          // Toggle entire block or visual
          chk.closest(".subtask-item").classList.toggle("done", chk.checked);
        } else {
          store.toggleSubtask(dayPlan.dayNumber, slot.id, subId);
          renderActiveDay();
        }
      });
    });

    // Add Custom Subtask
    const addSubBtn = card.querySelector(".add-sub-btn");
    const subInput = card.querySelector(".add-subtask-input");
    function handleAddSub() {
      const val = subInput.value.trim();
      if (val) {
        store.addSubtask(dayPlan.dayNumber, slot.id, val);
        subInput.value = "";
        renderActiveDay();
      }
    }
    addSubBtn.addEventListener("click", handleAddSub);
    subInput.addEventListener("keydown", e => {
      if (e.key === "Enter") handleAddSub();
    });

    // Quick Timer Actions
    card.querySelector(".slot-pomo-btn").addEventListener("click", () => {
      startPomodoroForSlot(slot.id, "POMODORO_25", 25);
    });
    card.querySelector(".slot-full-btn").addEventListener("click", () => {
      startPomodoroForSlot(slot.id, "FULL_BLOCK", slot.durationMinutes);
    });
    const mockScoreBtn = card.querySelector(".slot-mock-btn");
    if (mockScoreBtn) {
      mockScoreBtn.addEventListener("click", () => {
        openMockScoreModal(dayPlan.dayNumber);
      });
    }

    container.appendChild(card);
  });
}

// DAILY SUBJECT REVISION QUICK SECTION (Schedule Tab)
function renderDailyTopicRevisionSection(dayNum) {
  const container = document.getElementById("dailyTopicsList");
  container.innerHTML = "";

  const dayTopics = store.state.topics.filter(t => t.dayNumber === dayNum);
  if (dayTopics.length === 0) {
    container.innerHTML = `<p class="modal-sub">No topics assigned for Day ${dayNum}. Click "+ Add Topic" to add one.</p>`;
    return;
  }

  dayTopics.forEach(topic => {
    const row = document.createElement("div");
    row.className = "daily-topic-row";
    row.innerHTML = `
      <div class="daily-topic-info">
        <div class="daily-topic-subj">${topic.subject}</div>
        <div class="daily-topic-title">${topic.title}</div>
      </div>
      <div class="status-btn-group">
        <button class="status-pill-btn ${topic.status === "Mastered" ? "active mastered" : ""}" data-status="Mastered">Mastered</button>
        <button class="status-pill-btn ${topic.status === "Needs Review" ? "active review" : ""}" data-status="Needs Review">Needs Review</button>
        <button class="status-pill-btn ${topic.status === "Not Started" ? "active notstarted" : ""}" data-status="Not Started">Not Started</button>
      </div>
    `;

    row.querySelectorAll(".status-pill-btn").forEach(btn => {
      btn.addEventListener("click", () => {
        store.updateTopicStatus(topic.id, btn.dataset.status);
        renderDailyTopicRevisionSection(dayNum);
        renderTopicRevision();
      });
    });

    container.appendChild(row);
  });
}

// 10 AM to 10 PM VISUAL GANTT TIMELINE
function setupTimeline() {
  const track = document.getElementById("timelineTrack");
  const totalMinutes = 720; // 10 AM (600) to 10 PM (1320)

  DAILY_SLOTS.forEach(slot => {
    const seg = document.createElement("div");
    seg.className = `timeline-segment`;
    seg.dataset.slotId = slot.id;
    seg.title = `${slot.title} (${slot.startTime} – ${slot.endTime})`;

    const widthPct = (slot.durationMinutes / totalMinutes) * 100;
    seg.style.width = `${widthPct}%`;

    // Color by category
    const colors = {
      study: "#7c3aed",
      mock: "#9333ea",
      meal: "#f59e0b",
      walk: "#10b981",
      revision: "#d946ef",
      break: "#64748b"
    };
    seg.style.background = colors[slot.category] || "#6366f1";

    if (slot.durationMinutes >= 45) {
      seg.innerHTML = `<span>${slot.icon}</span>`;
    }

    seg.addEventListener("click", () => {
      document.querySelectorAll(".timeline-segment").forEach(s => s.classList.remove("active-selected"));
      seg.classList.add("active-selected");

      // Scroll to slot card
      const targetCard = document.getElementById(`card_${slot.id}`);
      if (targetCard) {
        targetCard.scrollIntoView({ behavior: "smooth", block: "center" });
        targetCard.style.outline = "2px solid var(--primary)";
        setTimeout(() => (targetCard.style.outline = "none"), 1800);
      }
    });

    track.appendChild(seg);
  });
}

function updateTimelineIndicator() {
  const indicator = document.getElementById("liveTimeIndicator");
  const now = new Date();
  const currentMinute = now.getHours() * 60 + now.getMinutes();

  // Schedule window: 10:00 (600) to 22:00 (1320)
  if (currentMinute >= 600 && currentMinute <= 1320) {
    const progress = (currentMinute - 600) / 720;
    indicator.style.display = "block";
    indicator.style.left = `${progress * 100}%`;
  } else {
    indicator.style.display = "none";
  }
}

// 5. POMODORO TIMER IMPLEMENTATION
function setupPomodoroTimer() {
  const slotSelect = document.getElementById("timerSlotSelect");
  slotSelect.innerHTML = DAILY_SLOTS.filter(s => !s.isBreak)
    .map(s => `<option value="${s.id}">${s.title} (${s.startTime})</option>`)
    .join("");

  slotSelect.addEventListener("change", e => {
    activeSlotId = e.target.value;
    updateTimerTopicInfo();
  });

  // Mode Chips
  document.querySelectorAll(".mode-chip").forEach(chip => {
    chip.addEventListener("click", () => {
      document.querySelectorAll(".mode-chip").forEach(c => c.classList.remove("active"));
      chip.classList.add("active");
      currentPomodoroMode = chip.dataset.mode;
      const duration = parseInt(chip.dataset.duration, 10);
      setTimerMode(currentPomodoroMode, duration);
    });
  });

  // Main Play / Pause Button
  document.getElementById("timerPlayPauseBtn").addEventListener("click", toggleTimer);
  document.getElementById("timerResetBtn").addEventListener("click", resetTimer);
  document.getElementById("timerSkipBtn").addEventListener("click", skipTimerPhase);
  document.getElementById("timerMinus5Btn").addEventListener("click", () => adjustTimer(-300));
  document.getElementById("timerPlus5Btn").addEventListener("click", () => adjustTimer(300));

  document.getElementById("timerCompleteSlotBtn").addEventListener("click", () => {
    store.toggleBlock(store.state.selectedDay, activeSlotId);
    renderActiveDay();
  });

  updateTimerTopicInfo();
  updateTimerDisplay();
  updatePomodoroStatsDisplay();
}

function updateTimerTopicInfo() {
  const slot = DAILY_SLOTS.find(s => s.id === activeSlotId) || DAILY_SLOTS[0];
  const dayPlan = DEFAULT_DAY_PLANS.find(p => p.dayNumber === store.state.selectedDay) || DEFAULT_DAY_PLANS[0];

  let topicText = slot.subtitle;
  if (slot.type === "STUDY_QUANT") topicText = dayPlan.quantTopic;
  else if (slot.type === "STUDY_REASONING") topicText = dayPlan.reasoningTopic;
  else if (slot.type === "STUDY_ENGLISH") topicText = dayPlan.englishTopic;
  else if (slot.type === "STUDY_GA") topicText = dayPlan.gaTopic;
  else if (slot.type === "MOCK_TEST") topicText = dayPlan.mockTestName;

  document.getElementById("timerTopicTitle").textContent = slot.title;
  document.getElementById("timerSubtitle").textContent = topicText;

  const isBreakMode = currentPomodoroMode.includes("BREAK");
  document.getElementById("pomodoroSessionType").textContent = isBreakMode ? "BREAK SESSION" : "FOCUS SESSION";
  document.getElementById("pomodoroSessionIcon").textContent = isBreakMode ? "☕" : "🍅";
}

function startPomodoroForSlot(slotId, mode, durationMinutes) {
  activeSlotId = slotId;
  const select = document.getElementById("timerSlotSelect");
  if (select) select.value = slotId;

  document.querySelectorAll(".mode-chip").forEach(c => {
    if (c.dataset.mode === mode) c.classList.add("active");
    else c.classList.remove("active");
  });

  currentPomodoroMode = mode;
  setTimerMode(mode, durationMinutes);
  updateTimerTopicInfo();

  // Jump to Pomodoro Tab
  document.querySelector('.nav-tab[data-tab="pomodoro"]').click();
  startTimer();
}

function setTimerMode(mode, durationMinutes) {
  stopTimer();
  if (durationMinutes === 0) {
    const slot = DAILY_SLOTS.find(s => s.id === activeSlotId);
    durationMinutes = slot ? slot.durationMinutes : 60;
  }
  timerTotalSeconds = durationMinutes * 60;
  timerSecondsRemaining = timerTotalSeconds;
  updateTimerDisplay();
  updateTimerTopicInfo();
}

function toggleTimer() {
  if (timerRunning) {
    pauseTimer();
  } else {
    startTimer();
  }
}

function startTimer() {
  if (timerRunning) return;
  timerRunning = true;

  document.querySelector(".play-icon").classList.add("hidden");
  document.querySelector(".pause-icon").classList.remove("hidden");
  document.getElementById("timerPlayPauseLabel").textContent = "Pause";
  document.getElementById("timerStateTag").textContent = "Running";
  document.getElementById("timerStateTag").style.color = "var(--accent-green)";

  const headerPill = document.getElementById("activeTimerBadgeBtn");
  headerPill.classList.remove("hidden");

  timerInterval = setInterval(() => {
    if (timerSecondsRemaining > 0) {
      timerSecondsRemaining--;
      updateTimerDisplay();
    } else {
      timerCompleted();
    }
  }, 1000);
}

function pauseTimer() {
  timerRunning = false;
  clearInterval(timerInterval);
  document.querySelector(".play-icon").classList.remove("hidden");
  document.querySelector(".pause-icon").classList.add("hidden");
  document.getElementById("timerPlayPauseLabel").textContent = "Resume";
  document.getElementById("timerStateTag").textContent = "Paused";
  document.getElementById("timerStateTag").style.color = "var(--accent-amber)";
}

function stopTimer() {
  pauseTimer();
  document.getElementById("timerPlayPauseLabel").textContent = "Start Focus";
  document.getElementById("timerStateTag").textContent = "Ready";
  document.getElementById("timerStateTag").style.color = "var(--primary)";
}

function resetTimer() {
  stopTimer();
  timerSecondsRemaining = timerTotalSeconds;
  updateTimerDisplay();
}

function adjustTimer(deltaSeconds) {
  timerSecondsRemaining = Math.max(60, timerSecondsRemaining + deltaSeconds);
  timerTotalSeconds = Math.max(timerTotalSeconds, timerSecondsRemaining);
  updateTimerDisplay();
}

function skipTimerPhase() {
  stopTimer();
  if (currentPomodoroMode.includes("BREAK")) {
    setTimerMode("POMODORO_25", 25);
  } else {
    setTimerMode("SHORT_BREAK", 5);
  }
  startTimer();
}

function timerCompleted() {
  stopTimer();

  // Play audio chime if enabled
  if (document.getElementById("soundToggle").checked) {
    audioService.playCompletionChime();
  }

  // Increment completed stats
  const mins = Math.round(timerTotalSeconds / 60);
  store.incrementPomodoroCycle(mins);
  updatePomodoroStatsDisplay();

  // Auto-mark slot completed
  store.toggleBlock(store.state.selectedDay, activeSlotId);
  renderActiveDay();

  alert(`⏰ Time's up! Great focus session on ${DAILY_SLOTS.find(s => s.id === activeSlotId)?.title || "Study Block"}.`);

  // Auto-suggest break
  if (!currentPomodoroMode.includes("BREAK")) {
    setTimerMode("SHORT_BREAK", 5);
  } else {
    setTimerMode("POMODORO_25", 25);
  }
}

function updateTimerDisplay() {
  const mins = Math.floor(timerSecondsRemaining / 60);
  const secs = timerSecondsRemaining % 60;
  const formatted = `${String(mins).padStart(2, "0")}:${String(secs).padStart(2, "0")}`;

  document.getElementById("timerCountdownText").textContent = formatted;
  document.getElementById("headerTimerCountdown").textContent = formatted;

  // SVG Progress Arc calculation (radius = 115, circumference = 2 * PI * 115 = 722.56)
  const circumference = 722.56;
  const progressRatio = timerTotalSeconds > 0 ? (timerTotalSeconds - timerSecondsRemaining) / timerTotalSeconds : 0;
  const offset = circumference - progressRatio * circumference;

  const circle = document.getElementById("timerProgressCircle");
  if (circle) {
    circle.style.strokeDashoffset = offset;
    circle.style.stroke = currentPomodoroMode.includes("BREAK") ? "var(--accent-green)" : "var(--primary)";
  }
}

function updatePomodoroStatsDisplay() {
  document.getElementById("statsCompletedCycles").textContent = store.state.pomodoroStats.completedCycles;
  document.getElementById("statsFocusMinutes").textContent = `${store.state.pomodoroStats.focusMinutes}m`;
}

// 6. 31-DAY STREAK & ROADMAP
function calculateStreak() {
  let currentStreak = 0;
  let bestStreak = 0;
  let currentChain = 0;
  const completedDays = new Set();

  for (let d = 1; d <= 31; d++) {
    const dayData = store.getDayData(d);
    // Consider a day complete if at least 1 study block was logged
    if (dayData.completedBlockIds.length > 0) {
      completedDays.add(d);
      currentChain++;
      if (currentChain > bestStreak) bestStreak = currentChain;
    } else {
      currentChain = 0;
    }
  }

  // Active current streak counting back from latest completed day
  let maxCompletedDay = 0;
  completedDays.forEach(d => {
    if (d > maxCompletedDay) maxCompletedDay = d;
  });

  if (maxCompletedDay > 0) {
    let day = maxCompletedDay;
    while (day >= 1 && completedDays.has(day)) {
      currentStreak++;
      day--;
    }
  }

  return {
    currentStreak,
    bestStreak,
    totalActiveDays: completedDays.size,
    completedDays
  };
}

function updateStreakUI() {
  const streak = calculateStreak();

  document.getElementById("headerStreakCount").textContent = streak.currentStreak;
  document.getElementById("streakNumberDisplay").textContent = streak.currentStreak;
  document.getElementById("streakFractionDisplay").textContent = `${streak.currentStreak}/31 Days`;
  document.getElementById("kpiCurrentStreak").textContent = `${streak.currentStreak} Days`;
  document.getElementById("kpiBestStreak").textContent = `${streak.bestStreak} Days`;
  document.getElementById("kpiRemainingDays").textContent = `${Math.max(0, 31 - streak.currentStreak)} Days`;
  document.getElementById("activeDaysCountText").textContent = `${streak.totalActiveDays} of 31 Days Active`;

  // Status Badge
  const badge = document.getElementById("streakStatusBadge");
  if (streak.currentStreak >= 31) badge.textContent = "🏆 31-Day Master Champion!";
  else if (streak.currentStreak >= 21) badge.textContent = "⚡ 21-Day Elite Discipline!";
  else if (streak.currentStreak >= 14) badge.textContent = "🔥 14-Day Halfway Milestone!";
  else if (streak.currentStreak >= 7) badge.textContent = "🚀 7-Day Unstoppable Habit!";
  else if (streak.currentStreak > 0) badge.textContent = `✨ ${streak.currentStreak} Days Unbroken`;
  else badge.textContent = "🌱 Day 1 Ready to Begin";

  // Render 31 Graduation Ticks around SVG Ring
  const ticksGroup = document.getElementById("streakTicksGroup");
  if (ticksGroup) {
    ticksGroup.innerHTML = "";
    const radius = 82;
    const center = 100;
    for (let i = 0; i < 31; i++) {
      const angleDeg = (i / 31) * 360;
      const angleRad = (angleDeg * Math.PI) / 180;
      const isTickCompleted = streak.completedDays.has(i + 1);

      const x1 = center + (radius - 8) * Math.cos(angleRad);
      const y1 = center + (radius - 8) * Math.sin(angleRad);
      const x2 = center + (radius + 8) * Math.cos(angleRad);
      const y2 = center + (radius + 8) * Math.sin(angleRad);

      const line = document.createElementNS("http://www.w3.org/2000/svg", "line");
      line.setAttribute("x1", x1);
      line.setAttribute("y1", y1);
      line.setAttribute("x2", x2);
      line.setAttribute("y2", y2);
      line.setAttribute("stroke", isTickCompleted ? "#f97316" : "rgba(148, 163, 184, 0.4)");
      line.setAttribute("stroke-width", isTickCompleted ? "2" : "1");
      ticksGroup.appendChild(line);
    }
  }

  // Update Progress Arc (radius 82, circumference = 2 * PI * 82 = 515.22)
  const circumference = 515.22;
  const arc = document.getElementById("streakProgressArc");
  if (arc) {
    const fraction = Math.min(1, streak.currentStreak / 31);
    arc.style.strokeDashoffset = circumference - fraction * circumference;
    arc.style.stroke = "#f97316";
  }

  // Render 31 Visual Continuity Ribbon Pills
  const ribbon = document.getElementById("streakRibbonPills");
  if (ribbon) {
    ribbon.innerHTML = "";
    for (let d = 1; d <= 31; d++) {
      const isDone = streak.completedDays.has(d);
      const pill = document.createElement("div");
      pill.className = `ribbon-day-pill ${isDone ? "completed streak-active" : ""}`;
      pill.innerHTML = `
        <span>D${d}</span>
        <span>${isDone ? "🔥" : "•"}</span>
      `;
      pill.addEventListener("click", () => {
        store.state.selectedDay = d;
        store.saveState();
        document.querySelector('.nav-tab[data-tab="schedule"]').click();
        renderActiveDay();
      });
      ribbon.appendChild(pill);
    }
  }

  // Render Roadmap Days
  renderRoadmapDays(0);
}

function renderRoadmapDays(filterPhase = 0) {
  const container = document.getElementById("roadmapDaysList");
  if (!container) return;
  container.innerHTML = "";

  const streak = calculateStreak();

  const filtered = filterPhase === 0 ? DEFAULT_DAY_PLANS : DEFAULT_DAY_PLANS.filter(p => p.phase === filterPhase);

  filtered.forEach(day => {
    const isCompleted = streak.completedDays.has(day.dayNumber);
    const card = document.createElement("div");
    card.className = "roadmap-day-card";
    card.innerHTML = `
      <div class="roadmap-day-header">
        <div>
          <span class="roadmap-day-number">DAY ${day.dayNumber} • PHASE ${day.phase}</span>
          <h4 class="roadmap-day-title">${day.title}</h4>
        </div>
        <span class="badge-pill ${isCompleted ? "badge-primary" : "badge-neutral"}">
          ${isCompleted ? "Completed ✓" : "Pending"}
        </span>
      </div>
      <div class="roadmap-subjects-grid">
        <div class="roadmap-subject-chip">
          <strong>📐 Quant</strong>
          <span>${day.quantTopic.split(". Solve")[0]}</span>
        </div>
        <div class="roadmap-subject-chip">
          <strong>🧩 Reasoning</strong>
          <span>${day.reasoningTopic.split(". Solve")[0]}</span>
        </div>
        <div class="roadmap-subject-chip">
          <strong>📖 English</strong>
          <span>${day.englishTopic.split(". Solve")[0]}</span>
        </div>
        <div class="roadmap-subject-chip">
          <strong>🌍 GA & CA</strong>
          <span>${day.gaTopic.split(". Solve")[0]}</span>
        </div>
      </div>
    `;

    card.addEventListener("click", () => {
      store.state.selectedDay = day.dayNumber;
      store.saveState();
      document.querySelector('.nav-tab[data-tab="schedule"]').click();
      renderActiveDay();
    });

    container.appendChild(card);
  });
}

// 7. TOPIC REVISION (124 TOPICS)
let topicSearchQuery = "";
let selectedSubjectFilter = "All";
let selectedStatusFilter = "All";

function setupTopicRevision() {
  const searchInput = document.getElementById("topicSearchInput");
  const clearBtn = document.getElementById("clearSearchBtn");

  searchInput.addEventListener("input", e => {
    topicSearchQuery = e.target.value.toLowerCase().trim();
    clearBtn.classList.toggle("hidden", !topicSearchQuery);
    renderTopicRevision();
  });

  clearBtn.addEventListener("click", () => {
    searchInput.value = "";
    topicSearchQuery = "";
    clearBtn.classList.add("hidden");
    renderTopicRevision();
  });

  // Subject Filter Pills
  document.querySelectorAll("#subjectFilterGroup .filter-pill").forEach(pill => {
    pill.addEventListener("click", () => {
      document.querySelectorAll("#subjectFilterGroup .filter-pill").forEach(p => p.classList.remove("active"));
      pill.classList.add("active");
      selectedSubjectFilter = pill.dataset.subject;
      renderTopicRevision();
    });
  });

  // Status Filter Pills
  document.querySelectorAll("#statusFilterGroup .filter-pill").forEach(pill => {
    pill.addEventListener("click", () => {
      document.querySelectorAll("#statusFilterGroup .filter-pill").forEach(p => p.classList.remove("active"));
      pill.classList.add("active");
      selectedStatusFilter = pill.dataset.status;
      renderTopicRevision();
    });
  });

  renderTopicRevision();
}

function renderTopicRevision() {
  // Update Mastery Stats Cards
  const subjects = ["Quantitative Aptitude", "Reasoning", "English", "General Awareness"];
  subjects.forEach(sub => {
    const list = store.state.topics.filter(t => t.subject === sub);
    const mastered = list.filter(t => t.status === "Mastered").length;
    const total = list.length || 31;
    const pct = Math.round((mastered / total) * 100);

    if (sub === "Quantitative Aptitude") {
      document.getElementById("quantMasteredCount").textContent = mastered;
      document.getElementById("quantProgressBar").style.width = `${pct}%`;
    } else if (sub === "Reasoning") {
      document.getElementById("reasoningMasteredCount").textContent = mastered;
      document.getElementById("reasoningProgressBar").style.width = `${pct}%`;
    } else if (sub === "English") {
      document.getElementById("englishMasteredCount").textContent = mastered;
      document.getElementById("englishProgressBar").style.width = `${pct}%`;
    } else if (sub === "General Awareness") {
      document.getElementById("gaMasteredCount").textContent = mastered;
      document.getElementById("gaProgressBar").style.width = `${pct}%`;
    }
  });

  // Filter Topics
  const filtered = store.state.topics.filter(t => {
    const matchSubject = selectedSubjectFilter === "All" || t.subject === selectedSubjectFilter;
    const matchStatus = selectedStatusFilter === "All" || t.status === selectedStatusFilter;
    const matchQuery =
      !topicSearchQuery ||
      t.title.toLowerCase().includes(topicSearchQuery) ||
      t.subject.toLowerCase().includes(topicSearchQuery) ||
      (t.notes && t.notes.toLowerCase().includes(topicSearchQuery));
    return matchSubject && matchStatus && matchQuery;
  });

  document.getElementById("topicResultsCount").textContent = `Showing ${filtered.length} of ${store.state.topics.length} topics`;

  const grid = document.getElementById("topicsGrid");
  grid.innerHTML = "";

  if (filtered.length === 0) {
    grid.innerHTML = `<div class="card" style="grid-column: 1/-1; text-align: center; padding: 40px;"><p>No topics match your search or filter.</p></div>`;
    return;
  }

  filtered.forEach(topic => {
    const card = document.createElement("div");
    card.className = "topic-card";

    let subjClass = "quant";
    if (topic.subject === "Reasoning") subjClass = "reasoning";
    else if (topic.subject === "English") subjClass = "english";
    else if (topic.subject === "General Awareness") subjClass = "ga";

    card.innerHTML = `
      <div>
        <div class="topic-card-top">
          <span class="subject-tag ${subjClass}">${topic.subject}</span>
          <span class="topic-day-badge">Day ${topic.dayNumber}</span>
        </div>
        <h4 class="topic-main-title">${topic.title}</h4>
      </div>

      <div>
        <div class="status-btn-group" style="margin-bottom: 8px;">
          <button class="status-pill-btn ${topic.status === "Mastered" ? "active mastered" : ""}" data-status="Mastered">Mastered</button>
          <button class="status-pill-btn ${topic.status === "Needs Review" ? "active review" : ""}" data-status="Needs Review">Needs Review</button>
          <button class="status-pill-btn ${topic.status === "Not Started" ? "active notstarted" : ""}" data-status="Not Started">Not Started</button>
        </div>

        <input type="text" class="topic-notes-input" placeholder="Add formula reminder or note..." value="${topic.notes || ""}">
      </div>
    `;

    card.querySelectorAll(".status-pill-btn").forEach(btn => {
      btn.addEventListener("click", () => {
        store.updateTopicStatus(topic.id, btn.dataset.status);
        renderTopicRevision();
      });
    });

    const noteInput = card.querySelector(".topic-notes-input");
    noteInput.addEventListener("change", e => {
      store.updateTopicNotes(topic.id, e.target.value.trim());
    });

    grid.appendChild(card);
  });
}

// 8. MOCK TESTS & SVG TREND CHART
function setupMockTests() {
  document.getElementById("logQuickMockBtn").addEventListener("click", () => {
    openMockScoreModal(store.state.selectedDay);
  });

  renderMockTests();
}

function renderMockTests() {
  const mocks = store.state.mockTests;
  const attempted = mocks.filter(m => m.isAttempted);

  // Overall Stats
  document.getElementById("mockAttemptedCount").innerHTML = `${attempted.length} <span class="sub-val">/ 31</span>`;

  let totalScoreSum = 0;
  let highest = 0;
  let accuracySum = 0;

  attempted.forEach(m => {
    totalScoreSum += m.totalScore;
    if (m.totalScore > highest) highest = m.totalScore;
    accuracySum += m.accuracyPercent || 0;
  });

  const avg = attempted.length > 0 ? (totalScoreSum / attempted.length).toFixed(1) : "0.0";
  const avgAcc = attempted.length > 0 ? Math.round(accuracySum / attempted.length) : 0;

  document.getElementById("mockAvgScore").innerHTML = `${avg} <span class="sub-val">/ 200</span>`;
  document.getElementById("mockHighestScore").innerHTML = `${highest.toFixed(1)} <span class="sub-val">/ 200</span>`;
  document.getElementById("mockAvgAccuracy").textContent = `${avgAcc}%`;

  // Draw Score Progression Trend Chart
  renderScoreTrendChart(mocks);

  // Render 31 Mock Cards Grid
  const grid = document.getElementById("mocksGrid");
  grid.innerHTML = "";

  mocks.forEach(m => {
    const card = document.createElement("div");
    card.className = `mock-card ${m.isAttempted ? "attempted" : ""}`;

    card.innerHTML = `
      <div>
        <div class="mock-card-top">
          <span class="mock-number-badge">MOCK TEST #${m.testNumber}</span>
          <span class="mock-provider-tag">${m.provider}</span>
        </div>
        <h4 class="mock-card-title">${m.title}</h4>
      </div>

      <div>
        <div class="mock-score-display">
          <span class="mock-total-num">${m.isAttempted ? m.totalScore.toFixed(1) : "--"}</span>
          <span class="mock-denom">/ 200 Marks</span>
        </div>

        <div class="sectional-pills-row">
          <span class="sectional-pill">Q: ${m.isAttempted ? m.quantScore : "0"}</span>
          <span class="sectional-pill">R: ${m.isAttempted ? m.reasoningScore : "0"}</span>
          <span class="sectional-pill">E: ${m.isAttempted ? m.englishScore : "0"}</span>
          <span class="sectional-pill">GA: ${m.isAttempted ? m.gaScore : "0"}</span>
        </div>
      </div>

      <div style="display: flex; justify-content: space-between; align-items: center; margin-top: 6px;">
        <span style="font-size: 11px; color: var(--text-muted);">
          ${m.isAttempted ? `Accuracy: ${m.accuracyPercent}%` : "Not attempted yet"}
        </span>
        <button class="btn btn-outline btn-sm log-mock-btn" data-test="${m.testNumber}">
          ${m.isAttempted ? "Edit Score" : "Log Score"}
        </button>
      </div>
    `;

    card.querySelector(".log-mock-btn").addEventListener("click", () => {
      openMockScoreModal(m.testNumber);
    });

    grid.appendChild(card);
  });
}

function renderScoreTrendChart(mocks) {
  const svg = document.getElementById("scoreTrendSvg");
  if (!svg) return;
  svg.innerHTML = "";

  const width = 800;
  const height = 240;
  const paddingLeft = 50;
  const paddingRight = 30;
  const paddingTop = 20;
  const paddingBottom = 40;

  const chartW = width - paddingLeft - paddingRight;
  const chartH = height - paddingTop - paddingBottom;

  // Max score scale = 200
  function getY(score) {
    return paddingTop + chartH - (score / 200) * chartH;
  }
  function getX(testIndex) {
    return paddingLeft + (testIndex / 30) * chartW;
  }

  // 1. Grid Lines (0, 50, 100, 150, 170, 200)
  [0, 50, 100, 150, 170, 200].forEach(score => {
    const y = getY(score);
    const line = document.createElementNS("http://www.w3.org/2000/svg", "line");
    line.setAttribute("x1", paddingLeft);
    line.setAttribute("y1", y);
    line.setAttribute("x2", width - paddingRight);
    line.setAttribute("y2", y);

    if (score === 170) {
      line.setAttribute("stroke", "#10b981");
      line.setAttribute("stroke-dasharray", "4,4");
      line.setAttribute("stroke-width", "1.5");
    } else if (score === 150) {
      line.setAttribute("stroke", "#f59e0b");
      line.setAttribute("stroke-dasharray", "4,4");
      line.setAttribute("stroke-width", "1");
    } else {
      line.setAttribute("stroke", "rgba(148, 163, 184, 0.15)");
      line.setAttribute("stroke-width", "1");
    }
    svg.appendChild(line);

    // Label
    const text = document.createElementNS("http://www.w3.org/2000/svg", "text");
    text.setAttribute("x", paddingLeft - 8);
    text.setAttribute("y", y + 4);
    text.setAttribute("text-anchor", "end");
    text.setAttribute("font-size", "10");
    text.setAttribute("fill", score >= 170 ? "#10b981" : "rgba(148, 163, 184, 0.6)");
    text.textContent = score;
    svg.appendChild(text);
  });

  // 170 Target Label
  const targetLabel = document.createElementNS("http://www.w3.org/2000/svg", "text");
  targetLabel.setAttribute("x", width - paddingRight);
  targetLabel.setAttribute("y", getY(170) - 6);
  targetLabel.setAttribute("text-anchor", "end");
  targetLabel.setAttribute("font-size", "10");
  targetLabel.setAttribute("font-weight", "700");
  targetLabel.setAttribute("fill", "#10b981");
  targetLabel.textContent = "170+ Target Cutoff";
  svg.appendChild(targetLabel);

  // X Axis Ticks (M1, M5, M10, M15, M20, M25, M31)
  [1, 5, 10, 15, 20, 25, 31].forEach(testNum => {
    const x = getX(testNum - 1);
    const text = document.createElementNS("http://www.w3.org/2000/svg", "text");
    text.setAttribute("x", x);
    text.setAttribute("y", height - 12);
    text.setAttribute("text-anchor", "middle");
    text.setAttribute("font-size", "10");
    text.setAttribute("fill", "rgba(148, 163, 184, 0.6)");
    text.textContent = `M${testNum}`;
    svg.appendChild(text);
  });

  // Plot Attempted Scores Line
  const points = [];
  mocks.forEach((m, idx) => {
    if (m.isAttempted) {
      points.push({ x: getX(idx), y: getY(m.totalScore), score: m.totalScore, num: m.testNumber });
    }
  });

  if (points.length > 0) {
    // Polyline
    if (points.length > 1) {
      const pathData = points.map((p, i) => `${i === 0 ? "M" : "L"} ${p.x} ${p.y}`).join(" ");
      const path = document.createElementNS("http://www.w3.org/2000/svg", "path");
      path.setAttribute("d", pathData);
      path.setAttribute("fill", "none");
      path.setAttribute("stroke", "#6366f1");
      path.setAttribute("stroke-width", "2.5");
      path.setAttribute("stroke-linecap", "round");
      path.setAttribute("stroke-linejoin", "round");
      svg.appendChild(path);
    }

    // Points
    points.forEach(p => {
      const circle = document.createElementNS("http://www.w3.org/2000/svg", "circle");
      circle.setAttribute("cx", p.x);
      circle.setAttribute("cy", p.y);
      circle.setAttribute("r", "4.5");
      circle.setAttribute("fill", "#6366f1");
      circle.setAttribute("stroke", "#ffffff");
      circle.setAttribute("stroke-width", "1.5");
      svg.appendChild(circle);
    });
  }
}

// 9. FORMULA CARDS
function setupFormulaCards() {
  const container = document.getElementById("formulasGrid");
  container.innerHTML = "";

  DEFAULT_FORMULA_CARDS.forEach(card => {
    const el = document.createElement("div");
    el.className = "formula-card";
    el.dataset.subject = card.subject;

    el.innerHTML = `
      <div class="formula-card-top">
        <span class="subject-tag ${card.subject === "Reasoning" ? "reasoning" : card.subject === "English" ? "english" : card.subject === "General Awareness" ? "ga" : "quant"}">
          ${card.subject}
        </span>
        <span class="badge-pill badge-neutral">${card.category}</span>
      </div>
      <h4 class="formula-card-title">${card.title}</h4>
      <p class="formula-card-content">${card.content}</p>
      ${card.rule ? `<div class="formula-highlight-box">${card.rule}</div>` : ""}
    `;

    container.appendChild(el);
  });

  // Filter Pills
  document.querySelectorAll("#formulaSubjectFilter .filter-pill").forEach(pill => {
    pill.addEventListener("click", () => {
      document.querySelectorAll("#formulaSubjectFilter .filter-pill").forEach(p => p.classList.remove("active"));
      pill.classList.add("active");
      const sub = pill.dataset.subject;
      document.querySelectorAll(".formula-card").forEach(c => {
        if (sub === "All" || c.dataset.subject === sub) {
          c.classList.remove("hidden");
        } else {
          c.classList.add("hidden");
        }
      });
    });
  });
}

// 10. MODALS HANDLING
function setupModals() {
  // Day Notes Modal
  const notesModal = document.getElementById("notesModal");
  document.getElementById("openNotesBtn").addEventListener("click", () => {
    const dayData = store.getDayData(store.state.selectedDay);
    document.getElementById("notesModalTitle").textContent = `Day ${store.state.selectedDay} Personal Study Notes`;
    document.getElementById("dayNotesTextarea").value = dayData.notes || "";
    notesModal.classList.remove("hidden");
  });
  document.getElementById("closeNotesModalBtn").addEventListener("click", () => notesModal.classList.add("hidden"));
  document.getElementById("cancelNotesBtn").addEventListener("click", () => notesModal.classList.add("hidden"));
  document.getElementById("saveNotesBtn").addEventListener("click", () => {
    store.saveDayNotes(store.state.selectedDay, document.getElementById("dayNotesTextarea").value);
    notesModal.classList.add("hidden");
  });

  // Add Custom Topic Modal
  const addTopicModal = document.getElementById("addTopicModal");
  const topicDaySelect = document.getElementById("topicDaySelect");
  topicDaySelect.innerHTML = Array.from({ length: 31 }, (_, i) => `<option value="${i + 1}">Day ${i + 1}</option>`).join("");

  function openAddTopic() {
    topicDaySelect.value = store.state.selectedDay;
    document.getElementById("topicTitleInput").value = "";
    document.getElementById("topicNotesInput").value = "";
    addTopicModal.classList.remove("hidden");
  }

  document.getElementById("openAddTopicModalBtn").addEventListener("click", openAddTopic);
  document.getElementById("addCustomTopicBtnSchedule").addEventListener("click", openAddTopic);
  document.getElementById("closeAddTopicModalBtn").addEventListener("click", () => addTopicModal.classList.add("hidden"));
  document.getElementById("cancelAddTopicBtn").addEventListener("click", () => addTopicModal.classList.add("hidden"));

  document.getElementById("saveAddTopicBtn").addEventListener("click", () => {
    const title = document.getElementById("topicTitleInput").value.trim();
    if (!title) {
      alert("Please enter a topic title.");
      return;
    }
    const day = topicDaySelect.value;
    const subject = document.getElementById("topicSubjectSelect").value;
    const status = document.getElementById("topicStatusSelect").value;
    const notes = document.getElementById("topicNotesInput").value.trim();

    store.addCustomTopic(day, subject, title, status, notes);
    addTopicModal.classList.add("hidden");
    renderActiveDay();
    renderTopicRevision();
  });

  // Mock Score Logger Modal
  setupMockScoreModal();

  // Reset Day & Mark All Done
  document.getElementById("resetDayBtn").addEventListener("click", () => {
    if (confirm(`Reset all block progress for Day ${store.state.selectedDay}?`)) {
      store.setAllDayBlocks(store.state.selectedDay, false);
      renderActiveDay();
    }
  });

  document.getElementById("completeAllDayBtn").addEventListener("click", () => {
    store.setAllDayBlocks(store.state.selectedDay, true);
    renderActiveDay();
  });
}

function setupMockScoreModal() {
  const modal = document.getElementById("mockScoreModal");
  const scoreInputs = ["scoreQuant", "scoreReasoning", "scoreEnglish", "scoreGA"];

  function calculateModalTotal() {
    let sum = 0;
    scoreInputs.forEach(id => {
      sum += parseFloat(document.getElementById(id).value) || 0;
    });
    document.getElementById("modalCalculatedTotal").textContent = `${sum.toFixed(1)} / 200`;

    const statusEl = document.getElementById("modalCutoffStatus");
    if (sum >= 170) {
      statusEl.textContent = "🏆 Top Post Guaranteed (170+ Elite)";
      statusEl.style.color = "#10b981";
    } else if (sum >= 150) {
      statusEl.textContent = "✓ Tier-1 Cutoff Safe (150+ Strong)";
      statusEl.style.color = "#f59e0b";
    } else {
      statusEl.textContent = "⚠️ Below 150: Need error book drill";
      statusEl.style.color = "#ef4444";
    }
  }

  scoreInputs.forEach(id => {
    document.getElementById(id).addEventListener("input", calculateModalTotal);
  });

  document.getElementById("closeMockModalBtn").addEventListener("click", () => modal.classList.add("hidden"));
  document.getElementById("cancelMockModalBtn").addEventListener("click", () => modal.classList.add("hidden"));

  document.getElementById("saveMockScoreBtn").addEventListener("click", () => {
    const testNum = parseInt(document.getElementById("mockModalTestNumber").value, 10);
    const q = parseFloat(document.getElementById("scoreQuant").value) || 0;
    const r = parseFloat(document.getElementById("scoreReasoning").value) || 0;
    const e = parseFloat(document.getElementById("scoreEnglish").value) || 0;
    const ga = parseFloat(document.getElementById("scoreGA").value) || 0;
    const total = q + r + e + ga;

    const attempted = parseInt(document.getElementById("mockModalAttemptedQs").value, 10) || 0;
    const timeTaken = parseInt(document.getElementById("mockModalTimeTaken").value, 10) || 60;
    const provider = document.getElementById("mockModalProvider").value;
    const weakTopics = document.getElementById("mockModalWeakTopics").value.trim();
    const errorAnalysis = document.getElementById("mockModalErrorAnalysis").value.trim();

    // Approximate accuracy
    const accuracy = attempted > 0 ? Math.min(100, Math.round(((total / 2) / attempted) * 100)) : 0;

    store.saveMockScore(testNum, {
      provider,
      quantScore: q,
      reasoningScore: r,
      englishScore: e,
      gaScore: ga,
      totalScore: total,
      attemptedQuestions: attempted,
      timeTakenMinutes: timeTaken,
      accuracyPercent: Math.max(0, accuracy),
      weakTopics,
      errorAnalysis
    });

    modal.classList.add("hidden");
    renderMockTests();
  });
}

function openMockScoreModal(testNumber) {
  const modal = document.getElementById("mockScoreModal");
  const mock = store.state.mockTests.find(m => m.testNumber === testNumber) || store.state.mockTests[0];

  document.getElementById("mockModalTestNumber").value = mock.testNumber;
  document.getElementById("mockModalTitle").textContent = `Log Score — Mock #${mock.testNumber}`;
  document.getElementById("mockModalSubtitle").textContent = mock.title;

  document.getElementById("mockModalProvider").value = mock.provider || "Testbook";
  document.getElementById("mockModalAttemptedQs").value = mock.attemptedQuestions || 85;
  document.getElementById("mockModalTimeTaken").value = mock.timeTakenMinutes || 60;

  document.getElementById("scoreQuant").value = mock.quantScore || 0;
  document.getElementById("scoreReasoning").value = mock.reasoningScore || 0;
  document.getElementById("scoreEnglish").value = mock.englishScore || 0;
  document.getElementById("scoreGA").value = mock.gaScore || 0;

  document.getElementById("mockModalWeakTopics").value = mock.weakTopics || "";
  document.getElementById("mockModalErrorAnalysis").value = mock.errorAnalysis || "";

  // Trigger total calc
  const q = mock.quantScore || 0;
  const r = mock.reasoningScore || 0;
  const e = mock.englishScore || 0;
  const ga = mock.gaScore || 0;
  const total = q + r + e + ga;
  document.getElementById("modalCalculatedTotal").textContent = `${total.toFixed(1)} / 200`;

  modal.classList.remove("hidden");
}
