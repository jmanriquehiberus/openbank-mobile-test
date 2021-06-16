package com.javiermp.openbankmobiletest.characters.viewmodel

sealed class CharactersNavigationCommand {
    object GoToDetail : CharactersNavigationCommand()
}