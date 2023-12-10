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

enum class FlashcardTable {
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
    DESCRIPTION {
        override fun toString(): String {
            return "description"
        }
    },
    USER_ID {
        override fun toString(): String {
            return "user_id"
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
    IS_PUBLIC {
        override fun toString(): String {
            return "is_public"
        }
    },
}
enum class CARDTable {
    ID {
        override fun toString(): String {
            return "id"
        }
    },
    FRONT {
        override fun toString(): String {
            return "front"
        }
    },
    BACK {
        override fun toString(): String {
            return "back"
        }
    },
    STATUS {
        override fun toString(): String {
            return "status"
        }
    },
    IS_LEARNED {
        override fun toString(): String {
            return "is_learned"
        }
    },
    FLASHCARD_ID {
        override fun toString(): String {
            return "flashcard_id"
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
}