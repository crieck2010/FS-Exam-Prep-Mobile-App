package com.example.model

enum class FsDomain(
    val title: String,
    val shortCode: String,
    val description: String,
    val targetExamQuestions: String
) {
    MATH_AND_SCIENCES(
        title = "Mathematics & Basic Sciences",
        shortCode = "MATH",
        description = "Algebra, trigonometry, statistics, error propagation, and basic physics",
        targetExamQuestions = "13–20 questions"
    ),
    SURVEY_METHODS_EQUIPMENT(
        title = "Surveying Methods and Field Equipment",
        shortCode = "EQUIP",
        description = "Total stations, leveling circuits, peg test, GNSS receivers, error types",
        targetExamQuestions = "15–23 questions"
    ),
    GEODESY_AND_PROJECTIONS(
        title = "Geodesy and Map Projections",
        shortCode = "GEOD",
        description = "Datums (NAD83, NAVD88), SPCS, UTM, Lambert & Transverse Mercator, scale factors",
        targetExamQuestions = "10–15 questions"
    ),
    BOUNDARY_LAW_PRINCIPLES(
        title = "Boundary Law and Professional Principles",
        shortCode = "LAW",
        description = "Conveyances, riparian rights, easements, unwritten rights, professional ethics",
        targetExamQuestions = "13–20 questions"
    ),
    PLSS(
        title = "Public Land Survey System (PLSS)",
        shortCode = "PLSS",
        description = "Aliquot parts acreage, fractional sections, lost vs obliterated corners",
        targetExamQuestions = "13–20 questions"
    ),
    APPLIED_MATH_COGO(
        title = "Applied Math and Unit Conversions",
        shortCode = "COGO",
        description = "Bearings/azimuths, coordinate geometry, traverse closure, Compass Rule",
        targetExamQuestions = "12–18 questions"
    ),
    CURVES(
        title = "Horizontal & Vertical Curves",
        shortCode = "CURV",
        description = "Arc/chord definition, circular curve geometry, parabolic vertical crest/sag",
        targetExamQuestions = "9–14 questions"
    ),
    PHOTOGRAMMETRY_GIS(
        title = "Photogrammetry, Remote Sensing & GIS",
        shortCode = "PHOTO",
        description = "Photo scale, relief displacement, flight lines, LiDAR, raster/vector GIS",
        targetExamQuestions = "7–11 questions"
    );

    companion object {
        fun fromId(id: String?): FsDomain? = entries.find { it.name == id }
    }
}

enum class QuestionDifficulty {
    EASIER,
    ABOUT_THE_SAME,
    HARDER
}
