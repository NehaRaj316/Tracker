package com.example.data.model

data class RevisionCard(
    val title: String,
    val subject: String,
    val category: String,
    val content: String,
    val formulaOrRule: String? = null
)

object RevisionData {
    val cards: List<RevisionCard> = listOf(
        // Quantitative Aptitude
        RevisionCard(
            title = "Percentage to Fraction Equivalents",
            subject = "Quantitative Aptitude",
            category = "Arithmetic",
            content = "1/2 = 50%, 1/3 = 33.33%, 1/4 = 25%, 1/5 = 20%, 1/6 = 16.67%, 1/7 = 14.28%, 1/8 = 12.5%, 1/9 = 11.11%, 1/11 = 9.09%, 1/12 = 8.33%, 1/14 = 7.14%, 1/16 = 6.25%",
            formulaOrRule = "% Change = (Change / Original) × 100"
        ),
        RevisionCard(
            title = "Successive Percentage & Discounts",
            subject = "Quantitative Aptitude",
            category = "Arithmetic",
            content = "Two successive changes a% and b% result in net change: a + b + (ab/100)%. For discounts, net discount = d1 + d2 - (d1 × d2 / 100)%.",
            formulaOrRule = "Net % = a + b + (ab/100)%"
        ),
        RevisionCard(
            title = "Simple & Compound Interest Differences",
            subject = "Quantitative Aptitude",
            category = "Arithmetic",
            content = "Difference between CI and SI for 2 years: D2 = P × (R/100)². For 3 years: D3 = P × (R/100)² × (3 + R/100).",
            formulaOrRule = "D2 = P(R/100)², D3 = P(R/100)²(3 + R/100)"
        ),
        RevisionCard(
            title = "Time, Speed & Distance / Boats",
            subject = "Quantitative Aptitude",
            category = "Arithmetic",
            content = "Average speed for equal distances: 2xy/(x+y). Upstream speed = (u - v), Downstream speed = (u + v). Boat speed in still water = (Downstream + Upstream)/2. Stream speed = (Downstream - Upstream)/2.",
            formulaOrRule = "km/h to m/s: multiply by 5/18; m/s to km/h: multiply by 18/5"
        ),
        RevisionCard(
            title = "Algebra Key Identities",
            subject = "Quantitative Aptitude",
            category = "Advance Maths",
            content = "a³ + b³ + c³ - 3abc = (a+b+c)(a² + b² + c² - ab - bc - ca) = 1/2(a+b+c)[(a-b)² + (b-c)² + (c-a)²]. If a + b + c = 0, then a³ + b³ + c³ = 3abc.",
            formulaOrRule = "If x + 1/x = k, then x² + 1/x² = k² - 2; x³ + 1/x³ = k³ - 3k"
        ),
        RevisionCard(
            title = "Geometry: Circles & Tangents Theorems",
            subject = "Quantitative Aptitude",
            category = "Advance Maths",
            content = "1. Tangent-Secant theorem: If PT is tangent and PAB is secant, PT² = PA × PB. 2. Two chords intersecting at P: PA × PB = PC × PD. 3. Alternate segment theorem: Angle between tangent and chord equals angle subtended by chord in opposite segment.",
            formulaOrRule = "Direct Common Tangent = √[d² - (r1 - r2)²]; Transverse Common Tangent = √[d² - (r1 + r2)²]"
        ),
        RevisionCard(
            title = "Trigonometric Special Values & Complementary",
            subject = "Quantitative Aptitude",
            category = "Advance Maths",
            content = "sin²θ + cos²θ = 1; 1 + tan²θ = sec²θ; 1 + cot²θ = cosec²θ. If A + B = 90°, then sin A = cos B, tan A · tan B = 1, sin²A + sin²B = 1. Max value of a sinθ + b cosθ = √(a² + b²), Min = -√(a² + b²).",
            formulaOrRule = "secθ + tanθ = x  =>  secθ - tanθ = 1/x"
        ),
        RevisionCard(
            title = "Mensuration 2D & 3D Formulas",
            subject = "Quantitative Aptitude",
            category = "Advance Maths",
            content = "Cone: CSA = πrl (where l = √(r² + h²)), Volume = 1/3 πr²h. Cylinder: CSA = 2πrh, TSA = 2πr(r+h), Vol = πr²h. Sphere: Surface Area = 4πr², Vol = 4/3 πr³. Hemisphere: CSA = 2πr², TSA = 3πr², Vol = 2/3 πr³.",
            formulaOrRule = "Equilateral Triangle: Area = (√3/4)a², Inradius = a/(2√3), Circumradius = a/√3"
        ),

        // General Intelligence & Reasoning
        RevisionCard(
            title = "Alphabet Positions & Reverse Pairs",
            subject = "Reasoning",
            category = "Coding-Decoding",
            content = "EJOTY (5, 10, 15, 20, 25). Reverse Pairs (Sum = 27): A-Z (Azad), B-Y (Boy), C-X (Crux), D-W (Dew), E-V (Evening), F-U (Fun), G-T (GT Road), H-S (High School), I-R (Indian Railway), J-Q (Jungle Queen), K-P (Kanpur), L-O (Love), M-N (Man).",
            formulaOrRule = "Reverse position = 27 - Forward position"
        ),
        RevisionCard(
            title = "Clock Angles & Calendar Rules",
            subject = "Reasoning",
            category = "Analytical",
            content = "Angle between hands = |30H - (11/2)M|. Hands coincide 11 times in 12 hours (22 times in 24 hrs). Hands opposite 11 times in 12 hrs. Hands at right angle 22 times in 12 hrs (44 times in 24 hrs). Normal year has 1 odd day; leap year has 2 odd days.",
            formulaOrRule = "Angle = |30H - 5.5M|"
        ),
        RevisionCard(
            title = "Dice Opposite Face Rules",
            subject = "Reasoning",
            category = "Non-Verbal",
            content = "Rule 1: If 1 face is common in two positions of a dice, rotate clockwise from that face on both dice; matching positions are opposites. Rule 2: If 2 faces are common, the remaining two uncommon faces are opposite to each other.",
            formulaOrRule = "Clockwise rotation from common face reveals all pairs"
        ),
        RevisionCard(
            title = "Syllogism Standard Deduction Rules",
            subject = "Reasoning",
            category = "Verbal Logic",
            content = "All + All = All. All + No = No. Some + All = Some. Some + No = Some-Not. 'Only a few A are B' means: Some A are B AND Some A are NOT B. 'Only A are B' means: All B are A (and B cannot be anything else).",
            formulaOrRule = "Complementary pairs for 'Either-Or': (Some + No) or (All + Some Not)"
        ),

        // English Comprehension
        RevisionCard(
            title = "Subject-Verb Agreement Golden Rules",
            subject = "English",
            category = "Grammar",
            content = "1. With 'along with', 'as well as', 'together with', verb agrees with the first subject. 2. With 'neither..nor', 'either..or', 'not only..but also', verb agrees with nearest subject. 3. 'One of + Plural Noun + Singular Verb' (e.g. One of the boys is). 4. 'The number of' takes singular verb; 'A number of' takes plural verb.",
            formulaOrRule = "Subject governs verb number; ignore intervening prepositional phrases"
        ),
        RevisionCard(
            title = "Conditional Sentences & Inversion",
            subject = "English",
            category = "Grammar",
            content = "Zero conditional: If + Present, Present. 1st: If + Simple Present, will + V1. 2nd: If + Simple Past, would + V1. 3rd: If + Past Perfect (had + V3), would have + V3. Inversion: When sentence begins with 'Hardly', 'Scarcely' (followed by 'when'), 'No sooner' (followed by 'than'), put auxiliary verb before subject.",
            formulaOrRule = "No sooner had he arrived THAN... (Never use 'when' with No Sooner)"
        ),
        RevisionCard(
            title = "Fixed Prepositions High-Yield List",
            subject = "English",
            category = "Grammar & Vocab",
            content = "Abide by, Abstain from, Accused of, Accustomed to, Acquitted of, Afraid of, Aim at, Senior/Junior/Inferior/Prefer to (never 'than'), Cope with (never 'cope up with'), Comply with, Congratulate on, Deprived of, Insist on, Refrain from.",
            formulaOrRule = "Latin comparatives (senior, junior, superior, prior) always take 'to'"
        ),
        RevisionCard(
            title = "High-Yield One-Word Substitutions (OWS)",
            subject = "English",
            category = "Vocabulary",
            content = "1. Altruist: One who works for others' welfare. 2. Omniscient: One knowing everything. 3. Inevitable: Certain to happen. 4. Ephemeral: Short-lived / transitory. 5. Fastidious: Hard to please. 6. Somnambulist: Sleepwalker. 7. Cannibal: One eating human flesh. 8. Anarchist: One seeking to abolish government.",
            formulaOrRule = "Prefixes: Omni (all), Phil (love), Mis (hatred), Somn (sleep), Chron (time)"
        ),

        // General Awareness
        RevisionCard(
            title = "Indian Constitution: Key Articles",
            subject = "General Awareness",
            category = "Indian Polity",
            content = "Art 14 (Equality before law), Art 17 (Abolition of Untouchability), Art 19 (6 Freedoms), Art 21 (Right to Life & Personal Liberty), Art 21A (Right to Education), Art 32 (Constitutional Remedies / Heart & Soul), Art 40 (Village Panchayats), Art 44 (Uniform Civil Code), Art 51A (Fundamental Duties), Art 72 (President's Pardon power), Art 110 (Money Bill), Art 123 (President's Ordinance), Art 280 (Finance Commission), Art 324 (Election Commission), Art 368 (Amendment).",
            formulaOrRule = "Emergency Articles: 352 (National), 356 (State/President's Rule), 360 (Financial)"
        ),
        RevisionCard(
            title = "Classical Dances & Exponents of India",
            subject = "General Awareness",
            category = "Static GK",
            content = "1. Bharatanatyam (Tamil Nadu): Rukmini Devi, Yamini Krishnamurthy. 2. Kathak (Uttar Pradesh): Birju Maharaj, Sitara Devi, Shovana Narayan. 3. Kathakali (Kerala): Kalamandalam Gopi. 4. Kuchipudi (Andhra Pradesh): Raja & Radha Reddy. 5. Odissi (Odisha): Kelucharan Mohapatra. 6. Manipuri (Manipur): Jhaveri sisters. 7. Mohiniyattam (Kerala): Sunanda Nair. 8. Sattriya (Assam): Sankaradeva.",
            formulaOrRule = "8 Recognized Classical Dances by Sangeet Natak Akademi"
        ),
        RevisionCard(
            title = "Major National Parks by State",
            subject = "General Awareness",
            category = "Static GK & Geography",
            content = "Kaziranga & Manas (Assam - One-horned rhino), Jim Corbett / Ramganga (Uttarakhand - 1st National Park 1936), Kanha & Bandhavgarh (Madhya Pradesh), Gir (Gujarat - Asiatic Lion), Ranthambore (Rajasthan), Sundarbans (West Bengal - Royal Bengal Tiger), Silent Valley & Periyar (Kerala), Keibul Lamjao (Manipur - World's only floating park).",
            formulaOrRule = "Madhya Pradesh has the highest number of National Parks in India"
        ),
        RevisionCard(
            title = "Human Body & Vitamins Deficiency",
            subject = "General Awareness",
            category = "General Science",
            content = "Vitamin A (Retinol) - Night blindness; Vitamin B1 (Thiamine) - Beriberi; Vitamin B3 (Niacin) - Pellagra; Vitamin B12 (Cyanocobalamin) - Pernicious anaemia; Vitamin C (Ascorbic acid) - Scurvy; Vitamin D (Calciferol) - Rickets; Vitamin K (Phylloquinone) - Failure in blood clotting. Water-soluble: B, C. Fat-soluble: A, D, E, K.",
            formulaOrRule = "Fat Soluble: K-E-D-A; Water Soluble: B & C"
        )
    )
}
