package logic;

/**
 * Enum zur Repräsentation von universellen Texten auf der Oberfläche.
 *
 * @author github.com/batscs
 */
public enum Token {

    EMPTY,
    // ************************************** Game Related Tokens *************************************,
    FIELD_HASE_MOVEAGAIN,
    FIELD_HASE_NEXTCARROT,
    FIELD_HASE_PREVIOUSCARROT,
    FIELD_HASE_FORWARDS,
    FIELD_HASE_EATSALAD,
    FIELD_HASE_BACKWARDS,
    FIELD_HASE_SUSPEND,
    FIELD_HASE_TAKEORGIVE,
    FIELD_HASE_REFUND,
    FIELD_HASE_IDLE_SUSPEND,
    FIELD_HASE_TAKEORGIVE_DO_NOTHING,
    FIELD_HASE_TAKEORGIVE_TAKE,
    FIELD_HASE_TAKEORGIVE_GIVE,
    FIELD_HASE_POSITION_UNCHANGED,

    FIELD_CARROT_CHOICE,
    FIELD_CARROT_GIVE,
    FIELD_CARROT_TAKE,
    FIELD_CARROT_CONTINUE_DRAWING,
    FIELD_CARROTS_CANT_GIVE,

    FIELD_SALAD_EAT,
    FIELD_NUMBER_GAINED_CARROTS,

    GAME_REACH_FINISH_UNSUCCESSFUL,
    GAME_REACH_FINISH_SUCCESSFUL,
    GAME_STATS_TURN,
    GAME_STATS_CARROTS,
    GAME_STATS_SALADS,
    GAME_REACH_FINISH_UNSUCCESSFUL_SALADS,
    GAME_REACH_FINISH_UNSUCCESSFUL_CARROTS,
    GAME_REACH_FINISH_UNSUCCESSFUL_ALL,
    GAME_REACH_FINISH_UNSUCCESSFUL_CANT_AFFORD,
    GAME_PLAYER_FORCE_RESET,

    // ********************************************* GUI Related Tokens *****************************************,
    GUI_BUTTON_START,
    GUI_LABEL_PLAYERNAMES,
    GUI_LABEL_PLAYERCOUNT,

    // ******************************************** Menu Bar Related Tokens ************************************,
    GUI_MENU_HEADER_FILE,
    GUI_MENU_ITEM_SAVE,
    GUI_MENU_ITEM_EXIT,
    GUI_MENU_ITEM_NEW,
    GUI_MENU_ITEM_LOAD,
    GUI_MENU_ITEM_START,

    GUI_MENU_HEADER_VIEW,
    GUI_MENU_ITEM_LANGUAGE_IMPORT,
    GUI_MENU_ITEM_LANGUAGE_RESET,

    GUI_MENU_HEADER_HELP,
    GUI_MENU_ITEM_HELP,
    GUI_MENU_ITEM_ABOUT,

    // ******************************************** Error Related Tokens ****************************************,
    ERROR_GAME_CANT_START_DUPLICATE_NAMES,
    ERROR_FILE_NO_FILE_SELECTED, ERROR_FILE_WRITE, ERROR_FILE_READ,
    ERROR_FILE_IMPORT_FILE_WRONG_JSON_FORMAT,

    ERROR_GAME_INVALID_DATA,

    ERROR_LANGUAGE_INVALID_DATA, ERROR_LANGUAGE_INVALID_SIZE,


    // ******************************************** Alert Related Tokens ****************************************,

    GUI_ALERT_ABOUT_TITLE, GUI_ALERT_ABOUT_CONTENT,

    GUI_ALERT_INFO_TITLE, GUI_ALERT_INFO_CONTENT,

    GAME_PLAYER_HAS_WON_TITLE, GAME_PLAYER_HAS_WON_CONTENT,

    GAME_PLAYER_SUSPENDED,

    TITLE_GUI_ERROR,
    TITLE_GUI_ALERT_GAME_EVENT,
    TITLE_GUI_MULTIPLE_CHOICE,

    HEADER_ERROR_FILE_LOADING,
    HEADER_ERROR_GAMEPLAY, HEADER_ERROR_LANGUAGE,

    GUI_INFO_LANGUAGE_RESET,
    GUI_INFO_LANGUAGE_RESET_DESCRIPTION,

    GUI_INFO_LANGUAGE_IMPORT,
    GUI_INFO_LANGUAGE_IMPORT_DESCRIPTION,

    GAME_CONFIRMATION_ABORT_MATCH_AND_RESET,
    GAME_CONFIRMATION_EXIT, GAME_CONFIRMATION_IMPORT,

    // ****************************************** FILE CHOOSER ****************************************************
    GUI_FILE_CHOOSER_EXTENSION_GAME_FILE,
    GUI_FILE_CHOOSER_EXTENSION_LANGUAGE_FILE

    // ************************************************************************************************************


}
