package com.example.data

import com.example.model.FsDomain

data class StudyTopic(
    val title: String,
    val domain: FsDomain,
    val keyFormulas: List<String>,
    val summary: String,
    val examTips: List<String>
)

data class FlashcardItem(
    val id: String,
    val domain: FsDomain,
    val frontTitle: String,
    val frontQuestion: String,
    val backAnswer: String,
    val formula: String? = null
)

object StudyGuideData {

    val topics: List<StudyTopic> = listOf(
        StudyTopic(
            title = "Circular Curves (Horizontal)",
            domain = FsDomain.CURVES,
            keyFormulas = listOf(
                "R = 5729.58 / D (Arc definition)",
                "T = R × tan(Δ / 2)",
                "L = 100 × (Δ / D) = R × Δ (in radians)",
                "LC = 2R × sin(Δ / 2)",
                "E = R × (sec(Δ / 2) - 1)",
                "M = R × (1 - cos(Δ / 2))"
            ),
            summary = "In highway and route surveying, horizontal curves ease directional transitions. The arc definition defines degree of curve D as the central angle subtended by a 100-foot arc.",
            examTips = listOf(
                "Remember 5729.58 comes from 180 × 100 / π.",
                "External E is the distance from PI to curve midpoint.",
                "Middle ordinate M is the distance from curve midpoint to long chord LC."
            )
        ),
        StudyTopic(
            title = "Curvature & Refraction Corrections",
            domain = FsDomain.SURVEY_METHODS_EQUIPMENT,
            keyFormulas = listOf(
                "C + R = 0.574 × M² (distance M in miles, result in feet)",
                "C + R = 0.0675 × K² (distance K in km, result in meters)",
                "Curvature makes distant objects appear lower by 0.667 × M².",
                "Atmospheric refraction bends light downward, offsetting curvature by 0.093 × M²."
            ),
            summary = "Earth curvature and atmospheric refraction affect all long-distance line-of-sight elevation determinations.",
            examTips = listOf(
                "Equal sight distances (balanced backsight and foresight) cancel curvature and refraction error automatically in differential leveling.",
                "Always verify whether units are in miles or thousands of feet."
            )
        ),
        StudyTopic(
            title = "PLSS Aliquot Parts & Section Breakdown",
            domain = FsDomain.PLSS,
            keyFormulas = listOf(
                "1 Township = 36 Sections = 6 miles × 6 miles = 23,040 acres",
                "1 Section = 640 acres = 1 square mile = 80 chains × 80 chains",
                "Quarter Section (1/4) = 160 acres",
                "Quarter-Quarter Section (1/4 of 1/4) = 40 acres",
                "1 chain = 66 ft = 4 rods = 100 links; 10 sq chains = 1 acre"
            ),
            summary = "The Public Land Survey System subdivides federal public domain lands using rectangular aliquot parts read from right to left.",
            examTips = listOf(
                "To calculate acreage, read description backwards: start at 640 acres and divide by denominators (e.g. N 1/2 of SW 1/4 of NE 1/4 = 640 / 4 / 4 / 2 = 20 acres).",
                "Section 16 (and 36) was historically reserved for public schools."
            )
        ),
        StudyTopic(
            title = "Geodetic Datums & Coordinate Systems",
            domain = FsDomain.GEODESY_AND_PROJECTIONS,
            keyFormulas = listOf(
                "Grid Distance = Ground Distance × Elevation Factor × Grid Scale Factor",
                "Elevation Factor = R / (R + h) where R ≈ 20,906,000 ft",
                "Combined Factor = Elevation Factor × Grid Scale Factor",
                "Orthometric Height H = Ellipsoid Height h - Geoid Height N (H = h - N)"
            ),
            summary = "State Plane Coordinate Systems (SPCS) project the curved earth onto developable surfaces: Lambert Conformal Conic for states elongated East-West, and Transverse Mercator for states elongated North-South.",
            examTips = listOf(
                "NAD 83 uses GRS 80 geocentric ellipsoid; NAD 27 uses Clarke 1866 non-geocentric.",
                "Vertical datum: NAVD 88 replaced NGVD 29."
            )
        ),
        StudyTopic(
            title = "Boundary Law & Priority of Calls",
            domain = FsDomain.BOUNDARY_LAW_PRINCIPLES,
            keyFormulas = listOf(
                "Priority 1: Senior rights (uncompromised senior grants)",
                "Priority 2: Natural monuments (rivers, ridges, marked trees)",
                "Priority 3: Artificial monuments (pipes, rebar, stones, pins)",
                "Priority 4: Bearings and Distances (metes and bounds)",
                "Priority 5: Area / Quantity (least reliable)"
            ),
            summary = "In resolving boundary ambiguities, the intent of original grantors controls, followed strictly by the standard common-law order of conflicting elements.",
            examTips = listOf(
                "Riparian: Accretion (gradual buildup) adds land; Avulsion (sudden cut) keeps old boundary.",
                "Adverse possession requires OPEN, NOTORIOUS, EXCLUSIVE, HOSTILE, CONTINUOUS possession for statutory period."
            )
        ),
        StudyTopic(
            title = "Traverse Adjustment (Compass Rule)",
            domain = FsDomain.APPLIED_MATH_COGO,
            keyFormulas = listOf(
                "Latitude = Length × cos(Azimuth or Bearing)",
                "Departure = Length × sin(Azimuth or Bearing)",
                "Linear Closure Error = √(ΣLat² + ΣDep²)",
                "Precision Ratio = 1 : (Perimeter / Linear Closure)",
                "Lat Correction = - (Course Length / Perimeter) × Total Lat Error",
                "Dep Correction = - (Course Length / Perimeter) × Total Dep Error"
            ),
            summary = "The Compass (Bowditch) Rule distributes traverse errors proportionally based on individual course lengths relative to total perimeter.",
            examTips = listOf(
                "Assumes angle and distance measurements have equal relative precision.",
                "Signs of corrections are always OPPOSITE to the signs of total misclosure."
            )
        )
    )

    val flashcards: List<FlashcardItem> = listOf(
        FlashcardItem(
            id = "fc_1",
            domain = FsDomain.CURVES,
            frontTitle = "Circular Curve Arc Definition",
            frontQuestion = "What is the formula for the radius R of a circular curve given the Degree of Curve D under the arc definition?",
            backAnswer = "R = 5729.58 / D\n\nDerived from: (100 / 2πR) = (D / 360°), therefore R = 18000 / (π × D) ≈ 5729.58 / D.",
            formula = "R = 5729.58 / D"
        ),
        FlashcardItem(
            id = "fc_2",
            domain = FsDomain.PLSS,
            frontTitle = "PLSS Section Acreage",
            frontQuestion = "How many acres are in the E 1/2 of the NW 1/4 of Section 12?",
            backAnswer = "80 acres.\n\nA full section is 640 acres. The NW 1/4 is 160 acres. The E 1/2 of that quarter is 80 acres.",
            formula = "640 / 4 / 2 = 80 acres"
        ),
        FlashcardItem(
            id = "fc_3",
            domain = FsDomain.SURVEY_METHODS_EQUIPMENT,
            frontTitle = "Combined Curvature & Refraction",
            frontQuestion = "What is the formula to compute combined earth curvature and refraction in US survey feet?",
            backAnswer = "C + R = 0.574 × M²\n\nWhere M is the distance in miles. For 2 miles: 0.574 × 4 = 2.30 ft.",
            formula = "C + R = 0.574 × M² (ft)"
        ),
        FlashcardItem(
            id = "fc_4",
            domain = FsDomain.BOUNDARY_LAW_PRINCIPLES,
            frontTitle = "Accretion vs Avulsion",
            frontQuestion = "How do Accretion and Avulsion affect riparian property boundaries?",
            backAnswer = "Accretion is the gradual, imperceptible deposit of soil that CHANGES the property boundary in favor of the riparian owner.\n\nAvulsion is a sudden, violent change in a river channel that DOES NOT change the original boundary line.",
            formula = "Accretion changes boundary; Avulsion preserves original boundary"
        ),
        FlashcardItem(
            id = "fc_5",
            domain = FsDomain.GEODESY_AND_PROJECTIONS,
            frontTitle = "SPCS Projection Orientations",
            frontQuestion = "Which map projection is typically used for states that have greater East-West vs North-South extent?",
            backAnswer = "Lambert Conformal Conic is used for states elongated East-West (e.g. Tennessee, Pennsylvania).\n\nTransverse Mercator is used for states elongated North-South (e.g. Illinois, Indiana, New Jersey).",
            formula = "E-W = Lambert Conic | N-S = Transverse Mercator"
        ),
        FlashcardItem(
            id = "fc_6",
            domain = FsDomain.APPLIED_MATH_COGO,
            frontTitle = "Compass (Bowditch) Rule",
            frontQuestion = "How does the Compass Rule distribute traverse misclosure to individual courses?",
            backAnswer = "It distributes the error in proportion to course length relative to total traverse perimeter:\n\nCorrection = - (Length_course / Total_perimeter) × Total_misclosure.",
            formula = "Corr = - (L_i / ΣL) × Error"
        )
    )
}
