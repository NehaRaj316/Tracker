package com.example.data.db

import com.example.data.model.DayPlan
import com.example.data.model.MockTest
import com.example.data.model.TopicRevision

object InitialData {
    fun get30DayPlans(): List<DayPlan> = listOf(
        // Phase 1: High-Weightage Core Foundations (Days 1 - 10)
        DayPlan(
            dayNumber = 1,
            title = "Percentages, Number Series & Ancient History",
            phase = 1,
            quantTopic = "Percentage: Fraction-to-percentage values (1/2 to 1/20), % increase/decrease, successive percentage change (a+b+ab/100). Solve 40 Qs.",
            reasoningTopic = "Analogy & Number Series: Prime number patterns, cubes/squares difference series, alphabet positional analogies. Solve 40 Qs.",
            englishTopic = "Grammar: Subject-Verb Agreement (Rules 1-10: 'either..or', 'neither..nor', collective nouns). Vocab: 50 high-frequency words + 20 Idioms.",
            gaTopic = "Ancient History: Indus Valley Civilization (sites, findings) & Vedic Culture. Current Affairs: Last Month national appointments & summits.",
            mockTestName = "Full Mock Test #1 (Tier-1 Diagnostic Assessment)"
        ),
        DayPlan(
            dayNumber = 2,
            title = "Profit & Loss, Coding-Decoding & Medieval India",
            phase = 1,
            quantTopic = "Profit, Loss & Discount: Cost price, selling price, marked price, successive discounts, dishonest dealer concept. Solve 40 Qs.",
            reasoningTopic = "Coding & Decoding: Letter coding (+/- shifts), number substitution, deciphering message codes. Solve 40 Qs.",
            englishTopic = "Grammar: Tenses (Simple vs Perfect, 'since/for', conditional sentences). Vocab: 50 words + One-Word Substitutions (OWS 1-30).",
            gaTopic = "Medieval History: Delhi Sultanate (Slave, Khilji, Tughlaq dynasties) & Mughal Empire administration. Current Affairs: Defence exercises & schemes.",
            mockTestName = "Full Mock Test #2 (Focus on Speed & Accuracy)"
        ),
        DayPlan(
            dayNumber = 3,
            title = "Ratio & Proportion, Syllogisms & Modern History",
            phase = 1,
            quantTopic = "Ratio, Proportion & Partnership: Duplicate/sub-duplicate ratios, mean proportional, profit distribution based on time & capital. Solve 35 Qs.",
            reasoningTopic = "Syllogisms: All, Some, No, Some-Not cases, Venn diagram method, Possibility conditions. Solve 40 Qs.",
            englishTopic = "Voice: Active & Passive Voice conversion rules for all tenses and interrogative sentences. Practice 30 transformations.",
            gaTopic = "Modern India: Revolt of 1857, Governor Generals/Viceroys, Indian National Congress sessions & key resolutions.",
            mockTestName = "Full Mock Test #3 (Sectional Time Management Drill)"
        ),
        DayPlan(
            dayNumber = 4,
            title = "Simple & Compound Interest, Blood Relations & Constitution",
            phase = 1,
            quantTopic = "SI & CI: Difference between CI and SI for 2 & 3 years, half-yearly/quarterly compounding, installment problems. Solve 35 Qs.",
            reasoningTopic = "Blood Relations: Family tree drawing method, coded blood relations (A+B means A is mother of B). Solve 35 Qs.",
            englishTopic = "Narration: Direct and Indirect Speech transformation rules (reporting verbs, tense shifts, pronoun changes). Practice 30 Qs.",
            gaTopic = "Indian Polity: Making of Constitution, Preamble, Fundamental Rights (Articles 12-35) & Fundamental Duties (Article 51A).",
            mockTestName = "Full Mock Test #4 (Target: Cut Down Unforced Errors)"
        ),
        DayPlan(
            dayNumber = 5,
            title = "Time & Work, Direction Sense & Indian Parliament",
            phase = 1,
            quantTopic = "Time & Work: LCM efficiency method, alternative day working, wages distribution, negative work (Pipes & Cisterns). Solve 40 Qs.",
            reasoningTopic = "Direction Sense & Distance: Pythagoras theorem applications, shadow angles (sunrise/sunset), turns & displacements. Solve 35 Qs.",
            englishTopic = "Grammar: Prepositions (Fixed prepositions list: 'accused of', 'prohibit from', 'senior to') + 2 Cloze Tests.",
            gaTopic = "Indian Polity: Parliament (Lok Sabha, Rajya Sabha), President's powers, ordinances, Constitutional Amendments (42nd, 44th, 73rd, 86th).",
            mockTestName = "Full Mock Test #5 (Milestone: First 5-Mock Review)"
        ),
        DayPlan(
            dayNumber = 6,
            title = "Speed, Time & Distance, Order & Ranking & Physical Geography",
            phase = 1,
            quantTopic = "Time, Speed & Distance: Relative speed, trains crossing poles/platforms, average speed (2xy/(x+y)), boats & streams. Solve 40 Qs.",
            reasoningTopic = "Order & Ranking: Position from left/right, overlapping cases, finding total persons in row, maximum & minimum. Solve 35 Qs.",
            englishTopic = "Vocabulary: 100 high-frequency Idioms & Phrases with Hindi/English meanings + Error spotting drill (25 Qs).",
            gaTopic = "Geography: Solar System, Earth layers, Rivers of India (Himalayan vs Peninsular systems, tributaries), Mountain passes.",
            mockTestName = "Full Mock Test #6 (Test Speed on Arithmetic)"
        ),
        DayPlan(
            dayNumber = 7,
            title = "Averages, Mixtures, Non-Verbal Reasoning & Economy",
            phase = 1,
            quantTopic = "Averages & Alligation: Weighted averages, batsman/bowler average, replacement rules, Rule of Alligation in mixtures. Solve 35 Qs.",
            reasoningTopic = "Non-Verbal: Mirror & Water images, Paper folding and cutting, Figure completion. Solve 45 Qs.",
            englishTopic = "Vocabulary: 100 most frequent One-Word Substitutions (CGL PYQs) + 1 Reading Comprehension passage drill.",
            gaTopic = "Indian Economy: GDP, GNP, NNP, Inflation types (CPI, WPI), Repo/Reverse Repo rates, Monetary & Fiscal policies.",
            mockTestName = "Full Mock Test #7 (Focus on Reasoning Accuracy)"
        ),
        DayPlan(
            dayNumber = 8,
            title = "Number System, Mathematical Operations & Physics",
            phase = 1,
            quantTopic = "Number System: Unit digit theorem, divisibility rules (7, 11, 13, 72, 88), remainder theorem, number of zeroes. Solve 40 Qs.",
            reasoningTopic = "Mathematical Operations: Sign interchanging, BODMAS rules in reasoning equations, balancing equations. Solve 40 Qs.",
            englishTopic = "Sentence Improvement: 30 PYQ questions focusing on modifiers, parallelism, and redundant expressions.",
            gaTopic = "General Science (Physics): SI Units, Newton's laws of motion, Work-Energy-Power, Optics (Reflection, Refraction, Lenses).",
            mockTestName = "Full Mock Test #8 (Check Divisibility & Number Speed)"
        ),
        DayPlan(
            dayNumber = 9,
            title = "Algebra Identities, Cubes & Dice & Chemistry",
            phase = 1,
            quantTopic = "Algebra: Core algebraic identities (a+b)^2, a^3+b^3+c^3-3abc, value putting methods, symmetry concepts. Solve 35 Qs.",
            reasoningTopic = "Cubes & Dice: Standard vs Ordinary dice, opposite face determination, open dice folding patterns. Solve 40 Qs.",
            englishTopic = "Para Jumbles: Sentence rearrangement strategy (identifying opening sentences, mandatory pairs, pronoun references).",
            gaTopic = "General Science (Chemistry): Periodic table trends, Acids-Bases-Salts (pH values), Important chemical formulas & ores.",
            mockTestName = "Full Mock Test #9 (Advance Maths Introduction)"
        ),
        DayPlan(
            dayNumber = 10,
            title = "Geometry Foundations, Statement-Assumptions & Biology",
            phase = 1,
            quantTopic = "Geometry: Triangles (congruence, similarity, centers: orthocenter, circumcenter, incenter, centroid). Solve 35 Qs.",
            reasoningTopic = "Critical Reasoning: Statement & Assumptions, Statement & Conclusions, Course of Action basics. Solve 30 Qs.",
            englishTopic = "Spelling & Confusing Words: 50 frequently misspelled words in SSC exams + 2 Cloze Tests.",
            gaTopic = "General Science (Biology): Human digestive, circulatory, nervous systems; Vitamins & deficiency diseases; Plant cell vs animal cell.",
            mockTestName = "Full Mock Test #10 (Phase 1 Benchmark Evaluation)"
        ),

        // Phase 2: Advance Speed Drills & Weak Link Elimination (Days 11 - 20)
        DayPlan(
            dayNumber = 11,
            title = "Trigonometry Basics, Classification & Static GK Art/Culture",
            phase = 2,
            quantTopic = "Trigonometry: Basic ratios, complementary angles (sin(90-θ)=cosθ), value table (0, 30, 45, 60, 90), basic identities. Solve 35 Qs.",
            reasoningTopic = "Classification & Odd One Out: Word, alphabet, and number odd-one-out speed sets. Solve 40 Qs.",
            englishTopic = "Error Spotting Marathon: 40 questions covering adjectives, adverbs, and degrees of comparison.",
            gaTopic = "Static GK: Classical Dances (8 forms & renowned exponents), Folk Dances state-wise, Major cultural festivals of India.",
            mockTestName = "Full Mock Test #11 (Target 130+ Marks Milestone)"
        ),
        DayPlan(
            dayNumber = 12,
            title = "Mensuration 2D, Figure Counting & Ecology/Sanctuaries",
            phase = 2,
            quantTopic = "Mensuration 2D: Triangles, rectangles, rhombus, trapezium, circles, semicircles, inscribed/circumscribed shapes. Solve 35 Qs.",
            reasoningTopic = "Figure Counting: Counting triangles, squares, rectangles, straight lines in complex geometric figures. Solve 35 Qs.",
            englishTopic = "Synonyms & Antonyms: 80 high-yield exam words with contextual example sentences.",
            gaTopic = "Environment & Ecology: National Parks, Wildlife Sanctuaries, Tiger Reserves, Ramsar wetland sites in India.",
            mockTestName = "Full Mock Test #12 (Strict 60-Minute CBT Simulation)"
        ),
        DayPlan(
            dayNumber = 13,
            title = "Mensuration 3D, Matrix & Static GK Superlatives",
            phase = 2,
            quantTopic = "Mensuration 3D: Cube, cuboid, cylinder, cone, sphere, hemisphere, frustum, prism & pyramid formulas. Solve 35 Qs.",
            reasoningTopic = "Matrix & Pattern Recognition: Grid puzzles, missing character in matrices and circles. Solve 40 Qs.",
            englishTopic = "Fill in the Blanks: Single and double blank PYQs testing vocabulary nuance and phrasal verbs.",
            gaTopic = "Static GK: First in India (Male/Female), First in World, Longest/Highest/Largest geographical records.",
            mockTestName = "Full Mock Test #13 (Identify Advance Math Blindspots)"
        ),
        DayPlan(
            dayNumber = 14,
            title = "Data Interpretation, Advanced Syllogisms & International Bodies",
            phase = 2,
            quantTopic = "Data Interpretation: Tables, Bar charts, Pie charts, Line graphs; fast calculation of % change and ratio. Solve 25 Qs.",
            reasoningTopic = "Advanced Syllogisms: 'Only a few', 'Only', 'Can never be' special statements and multi-conclusion verification. Solve 35 Qs.",
            englishTopic = "Reading Comprehension: 2 lengthy editorial passages (speed reading, tone identification, central theme).",
            gaTopic = "International Organizations: UN bodies, World Bank, IMF, WTO, BRICS, SCO, ASEAN (Headquarters & Chiefs).",
            mockTestName = "Full Mock Test #14 (Speed Test on DI & Data Sets)"
        ),
        DayPlan(
            dayNumber = 15,
            title = "Height & Distance, Coordinate Geometry & Awards/Honors",
            phase = 2,
            quantTopic = "Height & Distance + Coordinate: 30-60-90 & 45-45-90 standard ratios, distance formula, slope, equation of line. Solve 35 Qs.",
            reasoningTopic = "Mixed Reasoning Speed Sprint: 25 questions in 14 minutes strictly timed.",
            englishTopic = "Phrasal Verbs Marathon: 'Look into', 'Bring up', 'Put off', 'Give in' etc. with 30 practice questions.",
            gaTopic = "Awards & Honors: Nobel Prizes, Bharat Ratna, Padma Awards, Sahitya Akademi, Oscar & Filmfare awards summary.",
            mockTestName = "Full Mock Test #15 (Mid-Way Performance Audit)"
        ),
        DayPlan(
            dayNumber = 16,
            title = "Arithmetic PYQ Sprint, Venn Diagrams & Census 2011",
            phase = 2,
            quantTopic = "Arithmetic PYQ Sprint: Top 40 hardest percentage, profit & time-work questions from CGL 2022-2024 Tier 1/2.",
            reasoningTopic = "Venn Diagrams: Geometric Venn representations, finding distinct category intersections. Solve 35 Qs.",
            englishTopic = "Cloze Test Mastery: 4 sets of 5-question cloze tests with vocabulary elimination strategies.",
            gaTopic = "Census 2011: Literacy rates, sex ratio, population density, top/bottom states + Major central government schemes.",
            mockTestName = "Full Mock Test #16 (Benchmark: Aim for 140+ Marks)"
        ),
        DayPlan(
            dayNumber = 17,
            title = "Advance Algebra & Polynomials, Embedded Figures & Sports",
            phase = 2,
            quantTopic = "Algebra Advance: Polynomial roots, maximum/minimum value of quadratic expressions, reciprocal equations. Solve 35 Qs.",
            reasoningTopic = "Embedded Figures & Pattern Completion: Identifying hidden patterns in complex backgrounds. Solve 40 Qs.",
            englishTopic = "Active/Passive & Direct/Indirect Lightning Test: 30 questions in 15 minutes.",
            gaTopic = "Sports & Games: Terminology, number of players, trophies, Olympic/Asian Games/Commonwealth medals tally.",
            mockTestName = "Full Mock Test #17 (Focus on Fast Grammar Elimination)"
        ),
        DayPlan(
            dayNumber = 18,
            title = "Geometry Circles & Tangents, Clock/Calendar & Space Tech",
            phase = 2,
            quantTopic = "Geometry Circles: Alternate segment theorem, tangent-secant theorem (PA*PB=PT^2), common tangents. Solve 35 Qs.",
            reasoningTopic = "Clock & Calendar: Angle between hands, reflex angles, leap years, finding day for any historical date. Solve 35 Qs.",
            englishTopic = "Spotting Errors PYQs: 40 questions from recent CGL exams with detailed rule notes.",
            gaTopic = "Science & Tech: ISRO space missions (Chandrayaan, Aditya-L1, Gaganyaan), DRDO missile systems & satellites.",
            mockTestName = "Full Mock Test #18 (Tackle Advance Geometry)"
        ),
        DayPlan(
            dayNumber = 19,
            title = "Trigonometry Maxima/Minima, Logical Deduction & Current Affairs Part 1",
            phase = 2,
            quantTopic = "Trigonometric Identities: Max/min values (a sinθ + b cosθ), elimination of angles, compound angles. Solve 35 Qs.",
            reasoningTopic = "Logical Sequence of Words & Dictionary Order: Speed arrangement of words. Solve 35 Qs.",
            englishTopic = "One Word Substitution & Idioms Rapid Fire: 150 combined revision flashcards.",
            gaTopic = "Current Affairs Revision Part 1: National news, state schemes, MoUs, appointments of previous 3 months.",
            mockTestName = "Full Mock Test #19 (Target Sub-50 Minute Total Time)"
        ),
        DayPlan(
            dayNumber = 20,
            title = "Mensuration & DI Hybrid, Full Reasoning Sprint & Current Affairs Part 2",
            phase = 2,
            quantTopic = "Mensuration & Geometry Combined Drills: Solve 35 multi-concept questions with short tricks.",
            reasoningTopic = "Full Reasoning 25-Question Speed Drill: Target completion in 12 minutes with 100% accuracy.",
            englishTopic = "Grammar Comprehensive Test: 50 mixed error spotting and sentence improvement questions.",
            gaTopic = "Current Affairs Revision Part 2: International news, indices & rankings (Happiness, Press Freedom, HDI), summits.",
            mockTestName = "Full Mock Test #20 (Phase 2 Climax - Full Review)"
        ),

        // Phase 3: Real Exam Simulation, Rapid Revision & Tier-1 Readiness (Days 21 - 30)
        DayPlan(
            dayNumber = 21,
            title = "Top 50 Tough Arithmetic PYQ Sprint & Complete Polity Review",
            phase = 3,
            quantTopic = "Top 50 Tough Arithmetic Questions: Time & Distance, CI/SI installments, and Mixture alligations.",
            reasoningTopic = "Reasoning Speed Test: 25 questions in 11 minutes (focus on skipping time-trap questions).",
            englishTopic = "English Speed Test: 25 questions in 9 minutes (rapid scanning & elimination).",
            gaTopic = "Polity Rapid Fire: Important Articles (1 to 395 key list), Schedules 1 to 12, Emergency provisions.",
            mockTestName = "Full Mock Test #21 (Phase 3 Full CBT Marathon)"
        ),
        DayPlan(
            dayNumber = 22,
            title = "Advance Maths Speed Sprint & Freedom Struggle Revision",
            phase = 3,
            quantTopic = "Advance Maths Sprint: Geometry, Mensuration, Algebra & Trigonometry high-yield formulas test.",
            reasoningTopic = "Non-Verbal & Puzzles Speed Drill: 35 questions in 15 minutes.",
            englishTopic = "Complete 100 Golden Rules of Grammar Quick Review (Part 1).",
            gaTopic = "Modern History Rapid Fire: Timeline from 1905 (Partition of Bengal) to 1947 (Independence), Gandhi's movements.",
            mockTestName = "Full Mock Test #22 (Focus on Cut-off Clearance Strategy)"
        ),
        DayPlan(
            dayNumber = 23,
            title = "Calculation Tricks, Logic Drills & Geography Quick Scan",
            phase = 3,
            quantTopic = "Quick Calculation Shortcuts: Vedic math splits, digital sum method, approximations for DI.",
            reasoningTopic = "Analogy, Series & Coding Final Review: Review tricky patterns from error notebook.",
            englishTopic = "Complete 100 Golden Rules of Grammar Quick Review (Part 2) + 20 Phrasal Verbs.",
            gaTopic = "Geography Rapid Fire: Major Minerals, Dams & Reservoirs, Soil types, Biosphere reserves.",
            mockTestName = "Full Mock Test #23 (Test Pressure Management)"
        ),
        DayPlan(
            dayNumber = 24,
            title = "CGL 2024 Tier-1 Shift 1 Paper Emulation",
            phase = 3,
            quantTopic = "Solve official CGL 2024 Shift 1 Quant section in 20 minutes; analyze alternate shorter methods.",
            reasoningTopic = "Solve official CGL 2024 Shift 1 Reasoning section in 12 minutes.",
            englishTopic = "Solve official CGL 2024 Shift 1 English section in 10 minutes.",
            gaTopic = "Solve official CGL 2024 Shift 1 GA section in 6 minutes; revise all related static concepts.",
            mockTestName = "Full Mock Test #24 (Official PYQ Real Paper Simulation)"
        ),
        DayPlan(
            dayNumber = 25,
            title = "CGL 2024 Tier-1 Shift 2 Paper Emulation",
            phase = 3,
            quantTopic = "Solve official CGL 2024 Shift 2 Quant section in 20 minutes; identify time traps.",
            reasoningTopic = "Solve official CGL 2024 Shift 2 Reasoning section in 12 minutes.",
            englishTopic = "Solve official CGL 2024 Shift 2 English section in 10 minutes.",
            gaTopic = "Solve official CGL 2024 Shift 2 GA section in 6 minutes; review static GK topics asked.",
            mockTestName = "Full Mock Test #25 (Official PYQ Paper Simulation)"
        ),
        DayPlan(
            dayNumber = 26,
            title = "Formula Flashcards, Error Notebook Review & Static GK Marathon",
            phase = 3,
            quantTopic = "Quant Formula Notebook: Revise all 150+ formulas and shortcuts without solving new questions.",
            reasoningTopic = "Review all missed questions from Mocks 1-25 in the personal Error Notebook.",
            englishTopic = "Revise all 300+ bookmarked Vocab, Idioms & One-word substitutions.",
            gaTopic = "Static GK Marathon: Musical instruments & players, Temple architecture, Books & authors.",
            mockTestName = "Full Mock Test #26 (Target Personal Best Score)"
        ),
        DayPlan(
            dayNumber = 27,
            title = "Sectional Speed Master Class & Economy/Budget Scan",
            phase = 3,
            quantTopic = "Quant Speed Marathon: 25 arithmetic + 25 advance questions in two separate 20-min blocks.",
            reasoningTopic = "Reasoning Sprint: 2 full 25-question sectional mocks back to back.",
            englishTopic = "English Sprint: 2 full 25-question sectional mocks back to back.",
            gaTopic = "Economic Survey, Union Budget Highlights & GST basics quick scan.",
            mockTestName = "Full Mock Test #27 (Exam Timing Synchronization)"
        ),
        DayPlan(
            dayNumber = 28,
            title = "Confidence Booster Mock & Comprehensive Science Scan",
            phase = 3,
            quantTopic = "Review top 30 favorite question types in Quant; practice mental calculation.",
            reasoningTopic = "Light reasoning practice: Blood relations, Direction, Matrix.",
            englishTopic = "Rapid scan through Confusing Words & Spelling rules.",
            gaTopic = "Complete General Science Quick Scan (Physics units, Chemistry formulas, Biology vitamins).",
            mockTestName = "Full Mock Test #28 (High Confidence Test Series Simulation)"
        ),
        DayPlan(
            dayNumber = 29,
            title = "Mindset Calibration, Light Drills & Error Book Clean-up",
            phase = 3,
            quantTopic = "Light revision of arithmetic formulas; zero stress, no new concepts.",
            reasoningTopic = "Relaxed pattern scanning; 15 easy questions to maintain rhythm.",
            englishTopic = "Read 2 high-quality editorial articles to keep English reading comprehension fluid.",
            gaTopic = "Last 6 Months top 100 Current Affairs MCQs rapid glance.",
            mockTestName = "Full Mock Test #29 (Final Strict Full-Length Mock)"
        ),
        DayPlan(
            dayNumber = 30,
            title = "Final Day Formula Mastery & Exam Day Readiness",
            phase = 3,
            quantTopic = "Formula sheet read-through (Geometry theorems, Trigonometric table, Mensuration formulas).",
            reasoningTopic = "Mental relaxation; review test attempt strategy (Order: Reasoning -> GA -> English -> Quant).",
            englishTopic = "Light scan of golden grammar rules; positive affirmation.",
            gaTopic = "Static GK one-liners flip-through; keep mind fresh.",
            mockTestName = "Full Mock Test #30 (Final Confidence Booster & Exam Simulation)"
        )
    )

    fun get31DayPlans(): List<DayPlan> = get30DayPlans() + listOf(
        DayPlan(
            dayNumber = 31,
            title = "Grand Exam Eve & Tier-1 Exam Readiness",
            phase = 3,
            quantTopic = "Final formula handbook review: Geometry direct theorems, Mensuration 2D/3D, Algebra identities. Keep stress low.",
            reasoningTopic = "Quick glance at tricky alphabet coding and non-verbal pattern traps. Relaxed pacing.",
            englishTopic = "Last review of top 100 idioms, frequently confused words, and active voice rules.",
            gaTopic = "Review last 6 months high-priority current affairs list and important constitutional articles.",
            mockTestName = "Full Mock Test #31 (Grand Final Rehearsal & Score Lock-In)"
        )
    )

    fun get30MockTests(): List<MockTest> = (1..30).map { i ->
        val providerName = when {
            i % 5 == 1 -> "Testbook"
            i % 5 == 2 -> "Oliveboard"
            i % 5 == 3 -> "PYQ 2024 Shift"
            i % 5 == 4 -> "SuperCoaching"
            else -> "Adda247"
        }
        MockTest(
            testNumber = i,
            title = "SSC CGL Tier-1 Full Mock #$i",
            provider = providerName,
            isAttempted = false,
            quantScore = 0f,
            reasoningScore = 0f,
            englishScore = 0f,
            gaScore = 0f,
            totalScore = 0f,
            totalQuestions = 100,
            attemptedQuestions = 0,
            correctAnswers = 0,
            wrongAnswers = 0,
            accuracyPercent = 0f,
            timeTakenMinutes = 60,
            errorAnalysis = "",
            weakTopics = ""
        )
    }

    fun get31MockTests(): List<MockTest> = (1..31).map { i ->
        val providerName = when {
            i % 5 == 1 -> "Testbook"
            i % 5 == 2 -> "Oliveboard"
            i % 5 == 3 -> "PYQ 2024 Shift"
            i % 5 == 4 -> "SuperCoaching"
            else -> "Adda247"
        }
        MockTest(
            testNumber = i,
            title = "SSC CGL Tier-1 Full Mock #$i",
            provider = providerName,
            isAttempted = false,
            quantScore = 0f,
            reasoningScore = 0f,
            englishScore = 0f,
            gaScore = 0f,
            totalScore = 0f,
            totalQuestions = 100,
            attemptedQuestions = 0,
            correctAnswers = 0,
            wrongAnswers = 0,
            accuracyPercent = 0f,
            timeTakenMinutes = 60,
            errorAnalysis = "",
            weakTopics = ""
        )
    }

    fun getDefaultTopicRevisions(): List<TopicRevision> {
        val days = get31DayPlans()
        val list = mutableListOf<TopicRevision>()
        for (day in days) {
            list.add(
                TopicRevision(
                    dayNumber = day.dayNumber,
                    subject = "Quantitative Aptitude",
                    topicTitle = day.quantTopic.substringBefore(" Solve").substringBefore(" Practice").trim(),
                    status = "Not Started",
                    notes = ""
                )
            )
            list.add(
                TopicRevision(
                    dayNumber = day.dayNumber,
                    subject = "Reasoning",
                    topicTitle = day.reasoningTopic.substringBefore(" Solve").substringBefore(" Practice").trim(),
                    status = "Not Started",
                    notes = ""
                )
            )
            list.add(
                TopicRevision(
                    dayNumber = day.dayNumber,
                    subject = "English",
                    topicTitle = day.englishTopic.substringBefore(" Solve").substringBefore(" Practice").trim(),
                    status = "Not Started",
                    notes = ""
                )
            )
            list.add(
                TopicRevision(
                    dayNumber = day.dayNumber,
                    subject = "General Awareness",
                    topicTitle = day.gaTopic.substringBefore(" Solve").substringBefore(" Practice").trim(),
                    status = "Not Started",
                    notes = ""
                )
            )
        }
        return list
    }
}
