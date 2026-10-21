package com.example.model

enum class FsDomain(
    val slug: String,
    val title: String,
    val shortCode: String,
    val description: String
) {
    APPLIED_MATH_STATISTICS(
        slug = "applied-math-statistics",
        title = "Applied Math & Statistics",
        shortCode = "MATH",
        description = "Algebra, trigonometry, statistics, error propagation, and basic physics"
    ),
    BOUNDARY_LAW_REAL_PROPERTY(
        slug = "boundary-law-and-real-property",
        title = "Boundary Law & Real Property",
        shortCode = "LAW",
        description = "Conveyances, riparian rights, easements, unwritten rights, and PLSS"
    ),
    BUSINESS_CONCEPTS_ETHICS(
        slug = "business-concepts-ethics",
        title = "Business Concepts & Ethics",
        shortCode = "ETHICS",
        description = "Professional ethics, business practices, contracts, and liability"
    ),
    MAPPING_PROCESSES_METHODS(
        slug = "mapping-processes-and-methods",
        title = "Mapping Processes & Methods",
        shortCode = "MAP",
        description = "Photogrammetry, remote sensing, LiDAR, and GIS mapping"
    ),
    SURVEY_COMPUTATIONS(
        slug = "survey-computations",
        title = "Survey Computations",
        shortCode = "COMP",
        description = "COGO, coordinate geometry, traverse closure, horizontal and vertical curves"
    ),
    SURVEYING_PRINCIPLES_GEODESY(
        slug = "surveying-principles-geodesy",
        title = "Surveying Principles & Geodesy",
        shortCode = "GEOD",
        description = "Geodesy, datums, map projections, SPCS, and UTM"
    ),
    SURVEYING_PROCESSES_METHODS(
        slug = "surveying-processes-and-methods",
        title = "Surveying Processes & Methods",
        shortCode = "PROC",
        description = "Field procedures, total stations, leveling, GNSS, and equipment error"
    );

    companion object {
        fun fromId(id: String?): FsDomain? =
            entries.find { it.name.equals(id, ignoreCase = true) || it.slug.equals(id, ignoreCase = true) }

        fun fromSlug(slug: String?): FsDomain? =
            entries.find { it.slug == slug }
    }
}

enum class QuestionDifficulty {
    EASIER,
    ABOUT_THE_SAME,
    HARDER
}
