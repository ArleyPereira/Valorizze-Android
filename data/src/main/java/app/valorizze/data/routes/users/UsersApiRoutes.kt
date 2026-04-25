package app.valorizze.data.routes.users

object UsersApiRoutes {

    const val USERS_ROUTE = "users"

    const val USERS_RESET_ROUTE = "$USERS_ROUTE/reset"

    const val USERS_DELETE_ROUTE = "$USERS_ROUTE/delete"

    fun findUserByIdRoute(id: Int) = "$USERS_ROUTE/$id"

    fun updateUserByIdRoute(id: Int) = "$USERS_ROUTE/$id"
}

