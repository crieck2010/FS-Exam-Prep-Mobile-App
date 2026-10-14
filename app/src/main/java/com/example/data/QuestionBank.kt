package com.example.data

import com.example.model.FsDomain
import com.example.model.Question
import com.example.model.QuestionDifficulty

object QuestionBank {

    val questions: List<Question> = listOf(
        // === HORIZONTAL & VERTICAL CURVES ===
        Question(
            id = "q_curv_01",
            domain = FsDomain.CURVES,
            difficulty = QuestionDifficulty.ABOUT_THE_SAME,
            questionNumberLabel = "Question 1",
            questionText = "What is the radius of a circular curve if the Degree of Curve (arc definition) is 4°?",
            options = listOf(
                "1432.40 ft",
                "1145.92 ft",
                "5729.58 ft",
                "2864.79 ft"
            ),
            correctIndex = 0,
            explanation = "Under the arc definition, the radius is calculated as R = 5729.58 / D, which yields 1432.395 ft ≈ 1432.40 ft.",
            formulaHighlight = "R = 5729.58 / D = 5729.58 / 4 = 1432.40 ft",
            topicTag = "Circular Curves (Arc Definition)"
        ),
        Question(
            id = "q_curv_02",
            domain = FsDomain.CURVES,
            difficulty = QuestionDifficulty.HARDER,
            questionNumberLabel = "Question 2",
            questionText = "An equal-tangent crest vertical curve has an entry grade of +3.0% and an exit grade of -2.0% over a length of 600 ft (6 stations). At what distance from the PVC is the high point located?",
            options = listOf(
                "360 ft",
                "300 ft",
                "240 ft",
                "400 ft"
            ),
            correctIndex = 0,
            explanation = "Rate of change r = (g2 - g1) / L = (-2.0 - 3.0) / 6 = -5/6 %/station. Distance to high point x = -g1 / r = - (3.0) / (-5/6) = 3.6 stations = 360 ft from PVC.",
            formulaHighlight = "x = -g1 / r = -(3.0) / ((-2 - 3)/6) = 3.6 stations = 360 ft",
            topicTag = "Vertical Curves (High Point)"
        ),
        Question(
            id = "q_curv_03",
            domain = FsDomain.CURVES,
            difficulty = QuestionDifficulty.EASIER,
            questionNumberLabel = "Question 3",
            questionText = "For a simple circular curve with radius R = 1,000.00 ft and intersection angle Δ = 30° 00' 00\", what is the tangent distance T?",
            options = listOf(
                "267.95 ft",
                "500.00 ft",
                "523.60 ft",
                "133.97 ft"
            ),
            correctIndex = 0,
            explanation = "Tangent distance T = R × tan(Δ / 2). For Δ = 30°, Δ / 2 = 15°. T = 1000 × tan(15°) = 1000 × 0.267949 = 267.95 ft.",
            formulaHighlight = "T = R × tan(Δ / 2) = 1,000 × tan(15°) = 267.95 ft",
            topicTag = "Circular Curve Tangent"
        ),
        Question(
            id = "q_curv_04",
            domain = FsDomain.CURVES,
            difficulty = QuestionDifficulty.ABOUT_THE_SAME,
            questionNumberLabel = "Question 4",
            questionText = "What is the length L of a circular curve with radius R = 800 ft and central deflection angle Δ = 45° 00'?",
            options = listOf(
                "628.32 ft",
                "558.50 ft",
                "800.00 ft",
                "314.16 ft"
            ),
            correctIndex = 0,
            explanation = "Curve length L = R × Δ (with Δ in radians) = 800 × (45 × π / 180) = 800 × (π / 4) = 200 × π ≈ 628.32 ft.",
            formulaHighlight = "L = (2πR × Δ) / 360° = (2 × π × 800 × 45) / 360 = 628.32 ft",
            topicTag = "Curve Length"
        ),

        // === PUBLIC LAND SURVEY SYSTEM (PLSS) ===
        Question(
            id = "q_plss_01",
            domain = FsDomain.PLSS,
            difficulty = QuestionDifficulty.ABOUT_THE_SAME,
            questionNumberLabel = "Question 5",
            questionText = "How many acres are contained within the N 1/2 of the SW 1/4 of the NE 1/4 of a standard PLSS section?",
            options = listOf(
                "10 acres",
                "80 acres",
                "20 acres",
                "40 acres"
            ),
            correctIndex = 2,
            explanation = "A standard section contains 640 acres. You divide by 4 for the NE 1/4 (160), by 4 for the SW 1/4 (40), and by 2 for the N 1/2, yielding 20 acres.",
            formulaHighlight = "Area = 640 / (4 × 4 × 2) = 640 / 32 = 20 acres",
            topicTag = "Aliquot Parts Breakdown"
        ),
        Question(
            id = "q_plss_02",
            domain = FsDomain.PLSS,
            difficulty = QuestionDifficulty.HARDER,
            questionNumberLabel = "Question 6",
            questionText = "According to the BLM Manual of Surveying Instructions, what is the required procedure to re-establish a lost interior section corner common to four sections?",
            options = listOf(
                "Single proportionate measurement between closest existing monuments along the meridian",
                "Double proportionate measurement using the nearest existing corners to the North, South, East, and West",
                "Grantee boundary agreement method",
                "Compass rule offset from the closing corner"
            ),
            correctIndex = 1,
            explanation = "Under BLM manual rules, lost corners of two intersecting lines (such as a section corner common to 4 sections) are restored by double proportionate measurement based on original record distances to the nearest controlling original corners in all four cardinal directions.",
            formulaHighlight = "Lost Interior Section Corner = Double Proportionate Measurement (N, S, E, W)",
            topicTag = "Lost vs Obliterated Corners"
        ),
        Question(
            id = "q_plss_03",
            domain = FsDomain.PLSS,
            difficulty = QuestionDifficulty.EASIER,
            questionNumberLabel = "Question 7",
            questionText = "In a standard PLSS township comprising 36 sections, which section is located immediately north of Section 12?",
            options = listOf(
                "Section 1",
                "Section 6",
                "Section 13",
                "Section 11"
            ),
            correctIndex = 0,
            explanation = "PLSS sections are numbered serpentine: top row runs 1 to 6 (East to West), second row runs 7 to 12 (West to East). Therefore, Section 1 is directly above Section 12 at the northeast corner of the township.",
            formulaHighlight = "Top tier: Sec 6 (W) ... Sec 1 (E). Second tier: Sec 7 (W) ... Sec 12 (E)",
            topicTag = "Township Section Numbering"
        ),
        Question(
            id = "q_plss_04",
            domain = FsDomain.PLSS,
            difficulty = QuestionDifficulty.ABOUT_THE_SAME,
            questionNumberLabel = "Question 8",
            questionText = "How many acres are contained in the S 1/2 of the SE 1/4 together with the NE 1/4 of Section 16?",
            options = listOf(
                "240 acres",
                "200 acres",
                "160 acres",
                "320 acres"
            ),
            correctIndex = 0,
            explanation = "S 1/2 of SE 1/4 = (640 / 4) / 2 = 160 / 2 = 80 acres. NE 1/4 = 640 / 4 = 160 acres. Total combined = 80 + 160 = 240 acres.",
            formulaHighlight = "Total = 80 + 160 = 240 acres",
            topicTag = "Combined Aliquot Parts"
        ),

        // === SURVEYING METHODS & FIELD EQUIPMENT ===
        Question(
            id = "q_equip_01",
            domain = FsDomain.SURVEY_METHODS_EQUIPMENT,
            difficulty = QuestionDifficulty.ABOUT_THE_SAME,
            questionNumberLabel = "Question 9",
            questionText = "What is the combined curvature and refraction correction for a line of sight spanning a distance of 2 miles?",
            options = listOf(
                "1.15 ft",
                "4.59 ft",
                "2.30 ft",
                "0.57 ft"
            ),
            correctIndex = 2,
            explanation = "The formula for combined curvature and refraction is C + R = 0.574 × M². For 2 miles, 0.574 × 2² = 2.296 ft ≈ 2.30 ft.",
            formulaHighlight = "C + R = 0.574 × M² = 0.574 × (2)² = 2.296 ft ≈ 2.30 ft",
            topicTag = "Curvature & Refraction"
        ),
        Question(
            id = "q_equip_02",
            domain = FsDomain.SURVEY_METHODS_EQUIPMENT,
            difficulty = QuestionDifficulty.ABOUT_THE_SAME,
            questionNumberLabel = "Question 10",
            questionText = "In a standard two-peg test to check the collimation error of an automatic level, pegs A and B are 200 ft apart. From midway between pegs, Rod A = 4.82 ft and Rod B = 5.22 ft. What is the true elevation difference between A and B?",
            options = listOf(
                "0.40 ft (B is 0.40 ft lower than A)",
                "0.40 ft (B is 0.40 ft higher than A)",
                "0.80 ft (B is 0.80 ft lower than A)",
                "0.20 ft (B is 0.20 ft higher than A)"
            ),
            correctIndex = 0,
            explanation = "When an optical level is placed exactly midway between two rods, collimation and earth curvature errors cancel out completely. True ΔElevation = 4.82 ft - 5.22 ft = -0.40 ft (Peg B is 0.40 ft lower than Peg A).",
            formulaHighlight = "True ΔElev = Backsight - Foresight (with equal sight distances) = 4.82 - 5.22 = -0.40 ft",
            topicTag = "Level Peg Test"
        ),
        Question(
            id = "q_equip_03",
            domain = FsDomain.SURVEY_METHODS_EQUIPMENT,
            difficulty = QuestionDifficulty.EASIER,
            questionNumberLabel = "Question 11",
            questionText = "Which type of measurement error follows Gaussian normal distribution and cannot be eliminated completely, only minimized by repeated observations?",
            options = listOf(
                "Random (accidental) errors",
                "Systematic errors",
                "Blunders (mistakes)",
                "Instrument calibration offsets"
            ),
            correctIndex = 0,
            explanation = "Random errors are accidental errors that follow the laws of probability. Systematic errors conform to mathematical/physical laws and can be modeled and corrected. Blunders are human mistakes.",
            formulaHighlight = "Random errors conform to normal bell curve; magnitude decreases with √n repetitions",
            topicTag = "Error Types"
        ),
        Question(
            id = "q_equip_04",
            domain = FsDomain.SURVEY_METHODS_EQUIPMENT,
            difficulty = QuestionDifficulty.HARDER,
            questionNumberLabel = "Question 12",
            questionText = "A 100-ft steel tape is standardized at 68°F and 10 lbs pull. A baseline is measured on a hot day at 98°F with 10 lbs tension. If the recorded length is 800.00 ft and thermal expansion coefficient is 0.0000065 / °F, what is the temperature-corrected length?",
            options = listOf(
                "800.16 ft",
                "799.84 ft",
                "800.31 ft",
                "799.69 ft"
            ),
            correctIndex = 0,
            explanation = "Correction Ct = L × k × (T - T0). ΔT = 98 - 68 = +30°F. Ct = 800 × 0.0000065 × 30 = +0.156 ft ≈ +0.16 ft. Corrected distance = 800.00 + 0.16 = 800.16 ft. (Because the tape expanded, each tape length was too long, measuring too few feet).",
            formulaHighlight = "Ct = L × k × (T - T0) = 800 × 0.0000065 × 30 = +0.156 ft",
            topicTag = "Steel Tape Thermal Correction"
        ),

        // === BOUNDARY LAW & PROFESSIONAL PRINCIPLES ===
        Question(
            id = "q_law_01",
            domain = FsDomain.BOUNDARY_LAW_PRINCIPLES,
            difficulty = QuestionDifficulty.ABOUT_THE_SAME,
            questionNumberLabel = "Question 13",
            questionText = "What legal doctrine applies when soil and sand are gradually and imperceptibly deposited on a riverbank by natural water flow, extending the riparian owner's boundary?",
            options = listOf(
                "Avulsion",
                "Accretion",
                "Reliction",
                "Adverse possession"
            ),
            correctIndex = 1,
            explanation = "Accretion is the gradual and imperceptible accumulation of land by natural deposits (alluvion). The riparian owner acquires title to accreted land. Reliction is gradual receding of water, while avulsion is sudden, violent change which does not alter boundaries.",
            formulaHighlight = "Accretion = Gradual imperceptible deposit of soil onto shoreline",
            topicTag = "Riparian Rights"
        ),
        Question(
            id = "q_law_02",
            domain = FsDomain.BOUNDARY_LAW_PRINCIPLES,
            difficulty = QuestionDifficulty.ABOUT_THE_SAME,
            questionNumberLabel = "Question 14",
            questionText = "Which estate in real property represents the highest and most complete degree of ownership, free from conditions or restrictions on inheritance?",
            options = listOf(
                "Life Estate",
                "Fee Simple Determinable",
                "Fee Simple Absolute",
                "Estate for Years"
            ),
            correctIndex = 2,
            explanation = "Fee Simple Absolute is the highest type of ownership interest recognized by law. It provides unconditional ownership, transferable during life or passing to heirs upon death without reversionary limitations.",
            formulaHighlight = "Fee Simple Absolute = Fullest bundle of rights with no termination conditions",
            topicTag = "Estates in Land"
        ),
        Question(
            id = "q_law_03",
            domain = FsDomain.BOUNDARY_LAW_PRINCIPLES,
            difficulty = QuestionDifficulty.ABOUT_THE_SAME,
            questionNumberLabel = "Question 15",
            questionText = "In the order of importance of conflicting title elements in boundary surveying (rules of construction), which element holds the highest legal precedence?",
            options = listOf(
                "Bearing and distance (metes and bounds call)",
                "Area or quantity stated in the deed",
                "Calls for senior right (uncompromised senior conveyances)",
                "Calls for natural monuments"
            ),
            correctIndex = 2,
            explanation = "Senior rights take precedence above all other conflicting deed elements because a grantor cannot convey that which they do not own. In the standard hierarchy of evidence: 1. Senior Rights, 2. Natural Monuments, 3. Artificial Monuments, 4. Bearings and Distances, 5. Area.",
            formulaHighlight = "Hierarchy: Senior Rights > Natural Monuments > Artificial Monuments > Direction/Distance > Area",
            topicTag = "Rules of Construction"
        ),
        Question(
            id = "q_law_04",
            domain = FsDomain.BOUNDARY_LAW_PRINCIPLES,
            difficulty = QuestionDifficulty.EASIER,
            questionNumberLabel = "Question 16",
            questionText = "Which legal right permits a utility company to install and maintain power poles across private land without possessing title to the land itself?",
            options = listOf(
                "Easement in Gross",
                "Easement Appurtenant",
                "Adverse Possession",
                "Fee Simple Defeasible"
            ),
            correctIndex = 0,
            explanation = "An Easement in Gross belongs to an individual or legal entity (such as a utility or railroad) personally rather than attaching to a neighboring dominant tenement estate.",
            formulaHighlight = "Easement in Gross = Commercial/personal right not tied to dominant parcel",
            topicTag = "Easements"
        ),

        // === GEODESY & MAP PROJECTIONS ===
        Question(
            id = "q_geod_01",
            domain = FsDomain.GEODESY_AND_PROJECTIONS,
            difficulty = QuestionDifficulty.ABOUT_THE_SAME,
            questionNumberLabel = "Question 17",
            questionText = "A horizontal ground distance is measured as 1,250.00 ft. If the elevation factor is 0.999820 and the grid scale factor is 0.999950, what is the resulting State Plane Grid distance?",
            options = listOf(
                "1,249.71 ft",
                "1,250.29 ft",
                "1,249.48 ft",
                "1,250.00 ft"
            ),
            correctIndex = 0,
            explanation = "Grid Distance = Ground Distance × Combined Factor. Combined Factor = Elevation Factor × Grid Scale Factor = 0.999820 × 0.999950 = 0.999770. Grid Distance = 1,250.00 × 0.999770 = 1,249.71 ft.",
            formulaHighlight = "Grid Dist = Ground Dist × (Elev Factor × Grid Scale Factor) = 1,250.00 × 0.999770009 = 1,249.71 ft",
            topicTag = "Grid vs Ground Distances"
        ),
        Question(
            id = "q_geod_02",
            domain = FsDomain.GEODESY_AND_PROJECTIONS,
            difficulty = QuestionDifficulty.HARDER,
            questionNumberLabel = "Question 18",
            questionText = "Which reference ellipsoid and origin coordinate system are utilized by the North American Datum of 1983 (NAD 83)?",
            options = listOf(
                "Clarke 1866 ellipsoid with origin at Meades Ranch, Kansas",
                "GRS 80 ellipsoid with an Earth-centered (geocentric) origin",
                "WGS 84 ellipsoid with origin at Greenwich Observatory",
                "Bessel 1841 ellipsoid with topo-centric origin"
            ),
            correctIndex = 1,
            explanation = "NAD 83 is an Earth-centered (geocentric) datum based on the Geodetic Reference System 1980 (GRS 80) ellipsoid. In contrast, NAD 27 used the Clarke 1866 ellipsoid fixed at Meades Ranch, Kansas.",
            formulaHighlight = "NAD 83: GRS 80 Ellipsoid, Geocentric Origin (Earth's mass center)",
            topicTag = "Geodetic Datums"
        ),
        Question(
            id = "q_geod_03",
            domain = FsDomain.GEODESY_AND_PROJECTIONS,
            difficulty = QuestionDifficulty.ABOUT_THE_SAME,
            questionNumberLabel = "Question 19",
            questionText = "What is the relationship between orthometric height H, ellipsoid height h, and geoid height N?",
            options = listOf(
                "H = h - N",
                "H = h + N",
                "h = H - N",
                "N = H + h"
            ),
            correctIndex = 0,
            explanation = "The fundamental geodetic height equation is h = H + N. Rearranging for orthometric height (elevation above the geoid/MSL) gives H = h - N.",
            formulaHighlight = "H = h - N (Elevation = Ellipsoid Height - Geoid Undulation)",
            topicTag = "Geoid and Ellipsoid Heights"
        ),
        Question(
            id = "q_geod_04",
            domain = FsDomain.GEODESY_AND_PROJECTIONS,
            difficulty = QuestionDifficulty.EASIER,
            questionNumberLabel = "Question 20",
            questionText = "In the Universal Transverse Mercator (UTM) projection system, how wide is each longitude zone?",
            options = listOf(
                "6 degrees",
                "3 degrees",
                "15 degrees",
                "10 degrees"
            ),
            correctIndex = 0,
            explanation = "The Earth is divided into 60 UTM zones of 6 degrees longitude width each, numbered from 1 to 60 starting at the 180° meridian moving East.",
            formulaHighlight = "UTM: 60 zones × 6° each = 360° total longitude",
            topicTag = "UTM Grid Zones"
        ),

        // === APPLIED MATH & COGO ===
        Question(
            id = "q_cogo_01",
            domain = FsDomain.APPLIED_MATH_COGO,
            difficulty = QuestionDifficulty.ABOUT_THE_SAME,
            questionNumberLabel = "Question 21",
            questionText = "A line has an azimuth from North of 234° 15'. What is its equivalent quadrant bearing?",
            options = listOf(
                "S 35° 45' W",
                "S 54° 15' W",
                "N 54° 15' E",
                "W 54° 15' S"
            ),
            correctIndex = 1,
            explanation = "An azimuth of 234° 15' lies in the third quadrant (Southwest, between 180° and 270°). Bearing = Azimuth - 180° = 234° 15' - 180° 00' = S 54° 15' W.",
            formulaHighlight = "Quadrant III: Bearing = Azimuth - 180° = 234° 15' - 180° = S 54° 15' W",
            topicTag = "Bearings & Azimuths"
        ),
        Question(
            id = "q_cogo_02",
            domain = FsDomain.APPLIED_MATH_COGO,
            difficulty = QuestionDifficulty.ABOUT_THE_SAME,
            questionNumberLabel = "Question 22",
            questionText = "A closed traverse has a perimeter of 2,400.00 ft and a linear misclosure in latitude of +0.12 ft. Under the Compass (Bowditch) Rule, what is the latitude correction for a 400.00 ft course?",
            options = listOf(
                "+0.02 ft",
                "-0.02 ft",
                "-0.08 ft",
                "+0.06 ft"
            ),
            correctIndex = 1,
            explanation = "Compass Rule correction = - (Course Length / Total Perimeter) × Total Misclosure. Correction = - (400 / 2400) × (+0.12 ft) = - (1/6) × 0.12 = -0.02 ft.",
            formulaHighlight = "Corr = - (L_course / L_total) × Misclosure = - (400 / 2400) × 0.12 = -0.02 ft",
            topicTag = "Compass Rule Traverse Adjustment"
        ),
        Question(
            id = "q_cogo_03",
            domain = FsDomain.APPLIED_MATH_COGO,
            difficulty = QuestionDifficulty.ABOUT_THE_SAME,
            questionNumberLabel = "Question 23",
            questionText = "A cross-section roadway cut has an end area of 120 sq ft at station 10+00 and 240 sq ft at station 11+00. Using the Average End Area method, what is the excavation volume in cubic yards?",
            options = listOf(
                "666.67 cu yd",
                "1,333.33 cu yd",
                "500.00 cu yd",
                "2,000.00 cu yd"
            ),
            correctIndex = 0,
            explanation = "Station distance L = 11+00 - 10+00 = 100 ft. Volume (cu ft) = (A1 + A2)/2 × L = (120 + 240)/2 × 100 = 180 × 100 = 18,000 cu ft. In cubic yards = 18,000 / 27 = 666.67 cu yd.",
            formulaHighlight = "V = ((A1 + A2) / 2 × L) / 27 = ((120 + 240)/2 × 100) / 27 = 666.67 cu yd",
            topicTag = "Earthwork & Volume"
        ),
        Question(
            id = "q_cogo_04",
            domain = FsDomain.APPLIED_MATH_COGO,
            difficulty = QuestionDifficulty.HARDER,
            questionNumberLabel = "Question 24",
            questionText = "A traverse has closure misclosure values of ΔLat = +0.06 ft and ΔDep = -0.08 ft over a total perimeter length of 5,000.00 ft. What is the precision ratio of closure?",
            options = listOf(
                "1 : 50,000",
                "1 : 35,700",
                "1 : 100,000",
                "1 : 25,000"
            ),
            correctIndex = 0,
            explanation = "Linear misclosure = √(0.06² + (-0.08)²) = √(0.0036 + 0.0064) = √0.0100 = 0.10 ft. Precision = 1 : (5000 / 0.10) = 1 : 50,000.",
            formulaHighlight = "Misclosure = √(0.06² + 0.08²) = 0.10 ft. Ratio = 5,000 / 0.10 = 1 : 50,000",
            topicTag = "Traverse Precision Ratio"
        ),

        // === MATHEMATICS & BASIC SCIENCES ===
        Question(
            id = "q_math_01",
            domain = FsDomain.MATH_AND_SCIENCES,
            difficulty = QuestionDifficulty.ABOUT_THE_SAME,
            questionNumberLabel = "Question 25",
            questionText = "Four independent measurements of a boundary line yield standard errors of ±0.04 ft, ±0.03 ft, ±0.02 ft, and ±0.01 ft. What is the estimated standard error of the total combined line length?",
            options = listOf(
                "±0.055 ft",
                "±0.100 ft",
                "±0.025 ft",
                "±0.030 ft"
            ),
            correctIndex = 0,
            explanation = "Error propagation for independent additions: E_total = √(e1² + e2² + e3² + e4²) = √(0.04² + 0.03² + 0.02² + 0.01²) = √(0.0016 + 0.0009 + 0.0004 + 0.0001) = √0.0030 = ±0.0547 ft ≈ ±0.055 ft.",
            formulaHighlight = "E_sum = √(0.04² + 0.03² + 0.02² + 0.01²) = √0.0030 ≈ ±0.055 ft",
            topicTag = "Error Propagation"
        ),
        Question(
            id = "q_math_02",
            domain = FsDomain.MATH_AND_SCIENCES,
            difficulty = QuestionDifficulty.EASIER,
            questionNumberLabel = "Question 26",
            questionText = "In triangle ABC, side a = 150.00 ft, angle A = 35° 00', and angle B = 65° 00'. What is the length of side b?",
            options = listOf(
                "236.95 ft",
                "180.20 ft",
                "125.40 ft",
                "310.50 ft"
            ),
            correctIndex = 0,
            explanation = "Using Law of Sines: a / sin(A) = b / sin(B) => b = a × sin(B) / sin(A) = 150.00 × sin(65°) / sin(35°) = 150.00 × 0.906308 / 0.573576 = 236.95 ft.",
            formulaHighlight = "b = a × sin(B) / sin(A) = 150 × sin(65°) / sin(35°) = 236.95 ft",
            topicTag = "Law of Sines"
        ),
        Question(
            id = "q_math_03",
            domain = FsDomain.MATH_AND_SCIENCES,
            difficulty = QuestionDifficulty.HARDER,
            questionNumberLabel = "Question 27",
            questionText = "The internal angles of a closed 7-sided polygon (heptagon) are measured. What must their theoretical sum equal?",
            options = listOf(
                "900°",
                "1,080°",
                "720°",
                "1,260°"
            ),
            correctIndex = 0,
            explanation = "Sum of interior angles of any n-sided polygon = (n - 2) × 180°. For n = 7, Sum = (7 - 2) × 180° = 5 × 180° = 900°.",
            formulaHighlight = "Sum = (n - 2) × 180° = (7 - 2) × 180° = 900° 00' 00\"",
            topicTag = "Polygon Interior Angle Sum"
        ),

        // === PHOTOGRAMMETRY, REMOTE SENSING & GIS ===
        Question(
            id = "q_photo_01",
            domain = FsDomain.PHOTOGRAMMETRY_GIS,
            difficulty = QuestionDifficulty.ABOUT_THE_SAME,
            questionNumberLabel = "Question 28",
            questionText = "A vertical aerial photo is exposed with a 6-inch focal length lens from an aircraft flying at 6,000 ft above MSL over terrain having an average elevation of 1,000 ft above MSL. What is the average photo scale?",
            options = listOf(
                "1:10,000",
                "1:12,000",
                "1:5,000",
                "1:8,000"
            ),
            correctIndex = 0,
            explanation = "Photo Scale S = f / (H - h). f = 6 inches = 0.50 ft. Flying height above ground = H - h = 6,000 - 1,000 = 5,000 ft. S = 0.50 / 5,000 = 1 / 10,000 (1:10,000).",
            formulaHighlight = "Scale = f / (H - h) = 0.5 ft / (6,000 - 1,000 ft) = 1 : 10,000",
            topicTag = "Aerial Photogrammetry Scale"
        ),
        Question(
            id = "q_photo_02",
            domain = FsDomain.PHOTOGRAMMETRY_GIS,
            difficulty = QuestionDifficulty.HARDER,
            questionNumberLabel = "Question 29",
            questionText = "On a vertical aerial photograph, in which direction does topographic relief displacement displace points situated on high terrain relative to the nadir (principal point)?",
            options = listOf(
                "Radially outward away from the nadir",
                "Radially inward toward the nadir",
                "Perpendicular to the flight line only",
                "Parallel to the direction of aircraft motion"
            ),
            correctIndex = 0,
            explanation = "Relief displacement d = (r × h) / H. Points higher than average terrain are displaced radially outward away from the nadir/principal point; points lower than average terrain are displaced radially inward.",
            formulaHighlight = "High terrain = Radially outward; Depressions = Radially inward",
            topicTag = "Relief Displacement"
        ),
        Question(
            id = "q_photo_03",
            domain = FsDomain.PHOTOGRAMMETRY_GIS,
            difficulty = QuestionDifficulty.EASIER,
            questionNumberLabel = "Question 30",
            questionText = "In Geographic Information Systems (GIS), which data model stores geographic features as discrete points, lines, and polygons defined by coordinates?",
            options = listOf(
                "Vector data model",
                "Raster grid model",
                "TIN surface model",
                "Voxel model"
            ),
            correctIndex = 0,
            explanation = "The Vector model represents spatial geometry as explicit coordinate pairs (vertices) defining points, polylines, and polygons. The Raster model represents space as a matrix of grid cells/pixels.",
            formulaHighlight = "Vector = Points, Lines, Polygons | Raster = Grid cells with values",
            topicTag = "GIS Data Models"
        ),
        Question(
            id = "q_photo_04",
            domain = FsDomain.PHOTOGRAMMETRY_GIS,
            difficulty = QuestionDifficulty.ABOUT_THE_SAME,
            questionNumberLabel = "Question 31",
            questionText = "What remote sensing technology measures distance by illuminating a target with pulsed laser light and measuring reflected pulses to generate high-density 3D point clouds?",
            options = listOf(
                "LiDAR (Light Detection and Ranging)",
                "RADAR (Radio Detection and Ranging)",
                "SONAR",
                "Multispectral satellite radiometry"
            ),
            correctIndex = 0,
            explanation = "LiDAR uses eye-safe pulsing lasers (often near-infrared) paired with high-precision airborne GNSS and IMU to measure ground topography and vegetation canopy in dense 3D point clouds.",
            formulaHighlight = "LiDAR = Pulsed laser time-of-flight 3D point cloud generation",
            topicTag = "LiDAR Remote Sensing"
        ),
        Question(
            id = "q_math_04",
            domain = FsDomain.MATH_AND_SCIENCES,
            difficulty = QuestionDifficulty.ABOUT_THE_SAME,
            questionNumberLabel = "Question 32",
            questionText = "A surveyor measures an angle six times with readings: 45° 10' 20\", 45° 10' 25\", 45° 10' 15\", 45° 10' 30\", 45° 10' 20\", and 45° 10' 10\". What is the sample mean angle?",
            options = listOf(
                "45° 10' 20\"",
                "45° 10' 22\"",
                "45° 10' 18\"",
                "45° 10' 25\""
            ),
            correctIndex = 0,
            explanation = "Seconds values are: 20, 25, 15, 30, 20, 10. Sum = 120 seconds. Mean = 120 / 6 = 20 seconds. Mean angle = 45° 10' 20\".",
            formulaHighlight = "Mean = (20 + 25 + 15 + 30 + 20 + 10) / 6 = 20\" => 45° 10' 20\"",
            topicTag = "Sample Mean & Statistics"
        )
    )

    fun getQuestionsForQuiz(
        domain: FsDomain?,
        count: Int,
        focusOnGrowth: Boolean,
        weakDomains: Set<FsDomain> = emptySet(),
        difficulty: QuestionDifficulty = QuestionDifficulty.ABOUT_THE_SAME
    ): List<Question> {
        val filtered = when {
            domain != null -> questions.filter { it.domain == domain }
            focusOnGrowth && weakDomains.isNotEmpty() -> questions.filter { it.domain in weakDomains }
            else -> questions
        }

        val pool = if (filtered.isEmpty()) questions else filtered
        return pool.shuffled().take(count.coerceAtLeast(1).coerceAtMost(pool.size))
    }

    /**
     * Generates a balanced mock exam question set covering all 8 domains per official NCEES FS specifications
     */
    fun getMockExamQuestions(count: Int = 32): List<Question> {
        val domainList = FsDomain.entries
        val perDomain = (count / domainList.size).coerceAtLeast(1)
        val selected = mutableListOf<Question>()

        domainList.forEach { domain ->
            val domainPool = questions.filter { it.domain == domain }.shuffled()
            selected.addAll(domainPool.take(perDomain))
        }

        // Fill remaining if needed
        if (selected.size < count) {
            val remaining = questions.filterNot { selected.contains(it) }.shuffled()
            selected.addAll(remaining.take(count - selected.size))
        }

        return selected.shuffled().take(count)
    }

    /**
     * Adaptive question selector: dynamically selects questions based on domain mastery
     */
    fun getAdaptiveQuestions(
        domainMastery: Map<FsDomain, Int>, // domain -> mastery % (0..100)
        count: Int = 10
    ): List<Question> {
        // Domains with lower mastery get higher priority
        val weightedPool = mutableListOf<Question>()

        FsDomain.entries.forEach { domain ->
            val mastery = domainMastery[domain] ?: 50
            val domainQuestions = questions.filter { it.domain == domain }

            if (mastery < 60) {
                // Focus on growth: pick easier and standard questions
                val easyAndMed = domainQuestions.filter { it.difficulty != QuestionDifficulty.HARDER }
                weightedPool.addAll(easyAndMed)
                weightedPool.addAll(domainQuestions) // double weight
            } else if (mastery >= 80) {
                // High mastery: introduce harder questions
                val hardQuestions = domainQuestions.filter { it.difficulty == QuestionDifficulty.HARDER }
                if (hardQuestions.isNotEmpty()) weightedPool.addAll(hardQuestions)
                else weightedPool.addAll(domainQuestions.take(1))
            } else {
                weightedPool.addAll(domainQuestions)
            }
        }

        val finalPool = if (weightedPool.isEmpty()) questions else weightedPool
        return finalPool.shuffled().distinctBy { it.id }.take(count)
    }
}
