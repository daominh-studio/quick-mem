package com.daominh.quickmem.utils

enum class Table {
    CARD {
        override fun toString(): String {
            return "card"
        }
    },
    FLASHCARD {
        override fun toString(): String {
            return "flashcard"
        }
    },
    USER {
        override fun toString(): String {
            return "user"
        }

    },
    FOLDER {
        override fun toString(): String {
            return "folder"
        }
    },
    ClASS {
        override fun toString(): String {
            return "class"
        }
    },
    CLASS_USER {
        override fun toString(): String {
            return "class_user"
        }
    },
    CLASS_FLASHCARD {
        override fun toString(): String {
            return "class_flashcard"
        }
    },
    FOLDER_FLASHCARD {
        override fun toString(): String {
            return "folder_flashcard"
        }
    },
}

enum class UserTable {
    ID {
        override fun toString(): String {
            return "id"
        }
    },
    NAME {
        override fun toString(): String {
            return "name"
        }
    },
    EMAIL {
        override fun toString(): String {
            return "email"
        }
    },
    USERNAME {
        override fun toString(): String {
            return "username"
        }
    },
    PASSWORD {
        override fun toString(): String {
            return "password"
        }
    },
    AVATAR {
        override fun toString(): String {
            return "avatar"
        }
    },
    ROLE {
        override fun toString(): String {
            return "role"
        }
    },
    BIRTHDAY {
        override fun toString(): String {
            return "birthday"
        }
    },
    CREATED_AT {
        override fun toString(): String {
            return "created_at"
        }
    },
    UPDATED_AT {
        override fun toString(): String {
            return "updated_at"
        }
    },
    DELETED_AT {
        override fun toString(): String {
            return "deleted_at"
        }
    },
    STATUS {
        override fun toString(): String {
            return "status"
        }
    },
}