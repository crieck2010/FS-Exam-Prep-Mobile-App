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
    val formula: String? = null,
    val memoryTrick: String? = null
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
        // CURVES
        FlashcardItem(
            id = "fc_curv_01",
            domain = FsDomain.CURVES,
            frontTitle = "Circular Curve Arc Definition Radius",
            frontQuestion = "What is the formula for the radius R of a circular curve given Degree of Curve D under the arc definition?",
            backAnswer = "R = 5729.58 / D\n\nUnder the arc definition, D is the central angle subtended by a 100-ft circular arc. Derived as R = (100 × 360°) / (2π D) = 5729.578 / D.",
            formula = "R = 5729.58 / D",
            memoryTrick = "5729.58 = 100 × 180 / π"
        ),
        FlashcardItem(
            id = "fc_curv_02",
            domain = FsDomain.CURVES,
            frontTitle = "Tangent Distance (T)",
            frontQuestion = "How do you calculate the tangent distance T from the PC/PT to the PI of a circular curve?",
            backAnswer = "T = R × tan(Δ / 2)\n\nWhere R is the curve radius and Δ is the central intersection angle (deflection angle between tangents).",
            formula = "T = R × tan(Δ / 2)",
            memoryTrick = "T for Tan of half-delta"
        ),
        FlashcardItem(
            id = "fc_curv_03",
            domain = FsDomain.CURVES,
            frontTitle = "External Distance vs Middle Ordinate",
            frontQuestion = "What is the geometric difference between External Distance (E) and Middle Ordinate (M)?",
            backAnswer = "External (E) is the distance from the PI to the midpoint of the circular curve: E = R × (sec(Δ/2) - 1).\n\nMiddle Ordinate (M) is the distance from the midpoint of the curve to the midpoint of the Long Chord: M = R × (1 - cos(Δ/2)).",
            formula = "E = R(sec(Δ/2) - 1) | M = R(1 - cos(Δ/2))",
            memoryTrick = "E is external (outside toward PI); M is middle (inside to chord)"
        ),
        FlashcardItem(
            id = "fc_curv_04",
            domain = FsDomain.CURVES,
            frontTitle = "Vertical Curve High/Low Point",
            frontQuestion = "How do you locate the station of the high point (summit) or low point (sag) on an equal-tangent parabolic vertical curve?",
            backAnswer = "x = -g1 / r\n\nWhere g1 is the initial grade in %/station, and r is the rate of grade change: r = (g2 - g1) / L (with L in stations). The distance x is measured from the PVC.",
            formula = "x = -g1 / r where r = (g2 - g1) / L",
            memoryTrick = "Set first derivative of parabolic elevation equation to 0"
        ),

        // PLSS
        FlashcardItem(
            id = "fc_plss_01",
            domain = FsDomain.PLSS,
            frontTitle = "PLSS Aliquot Parts Acreage",
            frontQuestion = "How do you determine the acreage of an aliquot part description like 'N 1/2 of SW 1/4 of NE 1/4 of Section 10'?",
            backAnswer = "Read from right to left starting with 640 acres in a standard section and divide by each fraction denominator:\n\n640 ÷ 4 (NE 1/4 = 160) ÷ 4 (SW 1/4 = 40) ÷ 2 (N 1/2) = 20 acres.",
            formula = "Area = 640 / (4 × 4 × 2) = 20 acres",
            memoryTrick = "Divide 640 sequentially by the denominators from right to left"
        ),
        FlashcardItem(
            id = "fc_plss_02",
            domain = FsDomain.PLSS,
            frontTitle = "Lost Corner vs Obliterated Corner",
            frontQuestion = "What is the legal difference between an Obliterated Corner and a Lost Corner in the PLSS?",
            backAnswer = "An Obliterated Corner has no remaining physical monument, but its original location can be recovered from reliable collateral evidence, witness marks, or testimony.\n\nA Lost Corner cannot be determined from original evidence or collateral records and MUST be re-established by proportionate measurement.",
            formula = "Obliterated = Proved by evidence | Lost = Restored by proportion",
            memoryTrick = "Evidence beats proportion"
        ),
        FlashcardItem(
            id = "fc_plss_03",
            domain = FsDomain.PLSS,
            frontTitle = "Single vs Double Proportionate Measurement",
            frontQuestion = "When is Double Proportionate Measurement required instead of Single Proportionate Measurement?",
            backAnswer = "Double Proportionate Measurement is used for lost corners common to four townships or four sections (corners of two intersecting lines) using controlling corners in all four cardinal directions (N, S, E, W).\n\nSingle Proportionate is used for corners along a single straight line (such as quarter-section corners).",
            formula = "Double: 4 cardinal directions (interior section corners) | Single: 2 controlling corners along line",
            memoryTrick = "4 sections = 4 directions = Double proportion"
        ),
        FlashcardItem(
            id = "fc_plss_04",
            domain = FsDomain.PLSS,
            frontTitle = "Meander Line Definition",
            frontQuestion = "Does a meander line run by the BLM create a legal property boundary?",
            backAnswer = "NO. Meander lines are run to delineate water courses and calculate acreage for federal land patents. Under common law, the actual body of water (mean high-water mark or thread of stream) is the true boundary, NOT the meander line.",
            formula = "Water edge (OHWM) = Boundary, NOT the meander line",
            memoryTrick = "Meander measures acreage; water governs title"
        ),

        // BOUNDARY LAW
        FlashcardItem(
            id = "fc_law_01",
            domain = FsDomain.BOUNDARY_LAW_PRINCIPLES,
            frontTitle = "Accretion, Reliction, and Avulsion",
            frontQuestion = "Define Accretion, Reliction, and Avulsion. How do they affect boundaries?",
            backAnswer = "Accretion: Gradual imperceptible deposit of soil onto a bank (riparian owner GAINS title).\n\nReliction: Gradual receding of water exposing dry land (riparian owner GAINS title).\n\nAvulsion: Sudden, violent change in a river channel (boundary DOES NOT change; remains in old abandoned bed).",
            formula = "Accretion & Reliction move boundary; Avulsion fixes old boundary",
            memoryTrick = "Gradual moves title; Violent preserves old line"
        ),
        FlashcardItem(
            id = "fc_law_02",
            domain = FsDomain.BOUNDARY_LAW_PRINCIPLES,
            frontTitle = "Priority of Calls in Deeds",
            frontQuestion = "List the standard order of importance for conflicting calls in boundary descriptions.",
            backAnswer = "1. Senior Rights (Uncompromised grants)\n2. Natural Monuments (Rivers, ridges, marked trees)\n3. Artificial Monuments (Iron rods, pipes, stone corners)\n4. Bearings and Distances (Metes & Bounds measurements)\n5. Area / Quantity (Least controlling element)",
            formula = "Senior Rights > Natural Mon > Artificial Mon > Direction/Distance > Area",
            memoryTrick = "Senior Nature Articulated Bearings in the Area"
        ),
        FlashcardItem(
            id = "fc_law_03",
            domain = FsDomain.BOUNDARY_LAW_PRINCIPLES,
            frontTitle = "Adverse Possession Elements",
            frontQuestion = "What are the required legal elements to establish title by adverse possession?",
            backAnswer = "Possession must be:\n1. Open and Notorious (visible to the world)\n2. Continuous (uninterrupted for statutory period)\n3. Exclusive (held solely by claimant)\n4. Hostile / Adverse (without owner permission under claim of right)\n5. Actual (physical occupation of the land)",
            formula = "O.C.E.A.N. (Open, Continuous, Exclusive, Adverse, Notorious)",
            memoryTrick = "Remember the acronym OCEAN"
        ),
        FlashcardItem(
            id = "fc_law_04",
            domain = FsDomain.BOUNDARY_LAW_PRINCIPLES,
            frontTitle = "Easement Appurtenant vs In Gross",
            frontQuestion = "What is the difference between an Easement Appurtenant and an Easement in Gross?",
            backAnswer = "An Easement Appurtenant benefits a specific adjacent parcel of land (dominant estate) and 'runs with the land' when transferred.\n\nAn Easement in Gross benefits an individual or commercial entity personally (e.g. pipeline, utility company) with no dominant estate.",
            formula = "Appurtenant = Runs with land parcel | In Gross = Personal/Commercial entity",
            memoryTrick = "Appurtenant attaches to land"
        ),

        // GEODESY & PROJECTIONS
        FlashcardItem(
            id = "fc_geod_01",
            domain = FsDomain.GEODESY_AND_PROJECTIONS,
            frontTitle = "Combined Scale Factor (CF)",
            frontQuestion = "How do you calculate State Plane Grid Distance from measured Horizontal Ground Distance?",
            backAnswer = "Grid Distance = Ground Distance × Combined Factor (CF)\n\nWhere CF = Elevation Factor × Grid Scale Factor.\nElevation Factor = R / (R + h). Grid Scale Factor depends on latitude or longitude offset from projection standard parallels/central meridian.",
            formula = "Grid = Ground × Elevation Factor × Grid Scale Factor",
            memoryTrick = "Ground to Grid shrinks as elevation rises above sea level"
        ),
        FlashcardItem(
            id = "fc_geod_02",
            domain = FsDomain.GEODESY_AND_PROJECTIONS,
            frontTitle = "Geoid Height & Orthometric Elevation",
            frontQuestion = "What is the relationship between Orthometric Height H, Ellipsoid Height h, and Geoid Height N?",
            backAnswer = "h = H + N  =>  H = h - N\n\nWhere H is orthometric height (elevation above sea level/geoid), h is ellipsoid height (from GPS/GNSS receiver), and N is geoid undulation (geoid height relative to ellipsoid).",
            formula = "H = h - N  (Elevation = Ellipsoid Height - Geoid Undulation)",
            memoryTrick = "Height = GPS height minus Geoid separation"
        ),
        FlashcardItem(
            id = "fc_geod_03",
            domain = FsDomain.GEODESY_AND_PROJECTIONS,
            frontTitle = "Lambert Conic vs Transverse Mercator",
            frontQuestion = "Which map projection is chosen for states that extend East-West vs North-South?",
            backAnswer = "Lambert Conformal Conic is used for regions with dominant East-West extent (utilizing two standard parallels of latitude).\n\nTransverse Mercator is used for regions with dominant North-South extent (utilizing a central meridian of longitude).",
            formula = "East-West = Lambert Conic | North-South = Transverse Mercator",
            memoryTrick = "Lambert Lays flat (East-West); Transverse is Tall (North-South)"
        ),
        FlashcardItem(
            id = "fc_geod_04",
            domain = FsDomain.GEODESY_AND_PROJECTIONS,
            frontTitle = "NAD 83 vs NAD 27 Datums",
            frontQuestion = "What are the primary differences between the NAD 83 and NAD 27 horizontal datums?",
            backAnswer = "NAD 83 is Earth-centered (geocentric) based on the GRS 80 ellipsoid with center of mass origin.\n\nNAD 27 was non-geocentric based on the Clarke 1866 ellipsoid with its origin surface-fixed at Meades Ranch, Kansas.",
            formula = "NAD 83: Geocentric, GRS 80 | NAD 27: Meades Ranch, Clarke 1866",
            memoryTrick = "83 is worldwide geocentric"
        ),

        // SURVEY METHODS & FIELD EQUIPMENT
        FlashcardItem(
            id = "fc_equip_01",
            domain = FsDomain.SURVEY_METHODS_EQUIPMENT,
            frontTitle = "Combined Curvature and Refraction",
            frontQuestion = "What is the formula for combined earth curvature and refraction in US survey feet?",
            backAnswer = "C + R = 0.574 × M²\n\nWhere M is line-of-sight distance in miles. For 1 mile, correction is 0.574 ft; for 2 miles: 0.574 × 4 = 2.296 ft.",
            formula = "C + R = 0.574 × M² (ft)  |  0.0675 × K² (meters)",
            memoryTrick = "Curvature drops 0.667; refraction lifts 0.093; net drop = 0.574 M²"
        ),
        FlashcardItem(
            id = "fc_equip_02",
            domain = FsDomain.SURVEY_METHODS_EQUIPMENT,
            frontTitle = "Two-Peg Test for Levels",
            frontQuestion = "How does placing a leveling instrument midway between two turning points eliminate collimation error?",
            backAnswer = "Because line of sight distances to the backsight and foresight are equal, the collimation line tilts by the exact same vertical distance at both rods (+e). The error subtracts out completely: (BS + e) - (FS + e) = BS - FS.",
            formula = "Equal sight distances balance and cancel collimation error",
            memoryTrick = "Equal sight lengths = Zero collimation error"
        ),
        FlashcardItem(
            id = "fc_equip_03",
            domain = FsDomain.SURVEY_METHODS_EQUIPMENT,
            frontTitle = "Steel Tape Temperature Correction",
            frontQuestion = "What is the formula to correct distance measured with a steel tape for temperature variation?",
            backAnswer = "Ct = L × k × (T - T0)\n\nWhere L is measured length, k is thermal coefficient (0.0000065 / °F for steel), T is observation temperature, and T0 is standard temperature (normally 68°F).",
            formula = "Ct = L × 0.0000065 × (T - 68°F)",
            memoryTrick = "Hot tape expands, measures too short, so ADD correction"
        ),

        // APPLIED MATH & COGO
        FlashcardItem(
            id = "fc_cogo_01",
            domain = FsDomain.APPLIED_MATH_COGO,
            frontTitle = "Compass (Bowditch) Rule Corrections",
            frontQuestion = "How are latitude and departure corrections calculated under the Compass Rule?",
            backAnswer = "Correction_Lat = - (Length_course / Perimeter_total) × Misclosure_Lat\n\nCorrection_Dep = - (Length_course / Perimeter_total) × Misclosure_Dep\n\nDistributes error proportionally based on course length relative to total perimeter.",
            formula = "Corr = - (L_i / ΣL) × Misclosure",
            memoryTrick = "Sign is always OPPOSITE to misclosure"
        ),
        FlashcardItem(
            id = "fc_cogo_02",
            domain = FsDomain.APPLIED_MATH_COGO,
            frontTitle = "Azimuth to Quadrant Bearing",
            frontQuestion = "How do you convert an azimuth of 245° 30' into a quadrant bearing?",
            backAnswer = "Azimuth 245° 30' is in Quadrant III (Southwest, between 180° and 270°).\n\nBearing = Azimuth - 180° = 245° 30' - 180° 00' = S 65° 30' W.",
            formula = "Q III: Bearing = Azimuth - 180° (S __°__ ' W)",
            memoryTrick = "Subtract 180° from azimuth in the SW quadrant"
        ),
        FlashcardItem(
            id = "fc_cogo_03",
            domain = FsDomain.APPLIED_MATH_COGO,
            frontTitle = "Average End Area Earthwork Volume",
            frontQuestion = "What is the Average End Area formula for earthwork volume in cubic yards?",
            backAnswer = "Volume (cu yd) = [ (A1 + A2) / 2 × L ] / 27\n\nWhere A1 and A2 are end areas in square feet, L is distance between stations in feet, and 27 converts cubic feet to cubic yards.",
            formula = "V = [ ((A1 + A2) / 2) × L ] / 27",
            memoryTrick = "Average areas × length ÷ 27"
        ),

        // MATHEMATICS & BASIC SCIENCES
        FlashcardItem(
            id = "fc_math_01",
            domain = FsDomain.MATH_AND_SCIENCES,
            frontTitle = "Error Propagation (Independent Sum)",
            frontQuestion = "What is the formula for the standard error of the sum of independent measurements?",
            backAnswer = "E_sum = √(e1² + e2² + e3² + ... + en²)\n\nWhen adding independent observations, variances add directly, so the combined standard error is the root-sum-of-squares.",
            formula = "E_sum = √(Σ e_i²)",
            memoryTrick = "Root sum of squares of individual errors"
        ),
        FlashcardItem(
            id = "fc_math_02",
            domain = FsDomain.MATH_AND_SCIENCES,
            frontTitle = "Interior Angles of a Closed Polygon",
            frontQuestion = "What is the formula for the theoretical sum of the interior angles of a closed polygon with n sides?",
            backAnswer = "Sum = (n - 2) × 180°\n\nFor a 5-sided pentagon: (5 - 2) × 180° = 540°. For a 6-sided hexagon: (6 - 2) × 180° = 720°.",
            formula = "Sum = (n - 2) × 180° 00' 00\"",
            memoryTrick = "Subtract 2 triangles, multiply by 180°"
        ),

        // PHOTOGRAMMETRY & GIS
        FlashcardItem(
            id = "fc_photo_01",
            domain = FsDomain.PHOTOGRAMMETRY_GIS,
            frontTitle = "Aerial Photo Scale Formula",
            frontQuestion = "What is the equation for average photographic scale (S) on a vertical aerial photograph?",
            backAnswer = "S = f / (H - h)\n\nWhere f is focal length of the camera lens, H is flying height of aircraft above datum, and h is average terrain elevation above datum. Units must match.",
            formula = "S = f / (H - h)",
            memoryTrick = "focal length over height above ground"
        ),
        FlashcardItem(
            id = "fc_photo_02",
            domain = FsDomain.PHOTOGRAMMETRY_GIS,
            frontTitle = "Topographic Relief Displacement",
            frontQuestion = "In what direction does relief displacement shift high terrain features on a vertical photograph?",
            backAnswer = "Relief displacement shifts points higher than the datum RADIALLY OUTWARD away from the nadir (principal point).\n\nPoints lower than datum are displaced RADIALLY INWARD.",
            formula = "d = (r × h) / H (r = radial distance from nadir)",
            memoryTrick = "High reaches out; Low sinks in"
        )
    )
}
