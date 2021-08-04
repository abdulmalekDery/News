package com.beinmedia.test.models.network.control.error

data class Errors(
    val global: String?,
    val phone: String?,
    val email: String?,
    val order_id:String,
    val bid: String?,

    val code: String?,
    var name: String?,
    var password: String?,
    var password_confirmation: String?,
    var account_type: String?,
    var registration_type: String?,
    var new_password_confirmation: String?,
    var new_phone: String?,
    var order_type: String?,
    var this_attribute_is_required: String?,
    var no_such_record: String?,
    var rating: String?,
    var comment: String?,
    var food_rating: String?,
    var user_type: String?,
    var gender: String?,
    var profile: String?,
    var degree: String?,
    var nationality: String?,
    var services: String?,
    var bank_name: String?,
    var account_holder_name: String?,
    var iban: String?,
    var account_number: String?,
    var birth_date: String?,
    var education_level: String?,
    var experience: String?,
    var due_date:String?

)