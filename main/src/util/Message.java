package util;
public enum Message {
    LOGGER_CONFIG_LOADING_ERROR("Не смог загрузить файл конфигурации для логера: "),
    CHECK_FILESYSTEM_ACCESS("Проверьте, что есть доступ к файловой системе"),
    DATA_LOADING_STARTED("Загрузка данных..."),
    PAGE_DATA_LOADED("Загружены данные по сранице: "),
    TOPIC_DATA_LOADING_ERROR("При загрузке данных топика возникла ошибка, проверьте работоспособность сети"),
    PAGE_DATA_LOADING_ERROR("При загрузке данных страницы возникла ошибка, проверьте работоспособность сети"),
    DATA_LOADING_COMPLETED("Загрузка данных успешно завершена"),
    DATA_PROCESSING_STARTED("Обработка данных..."),
    DATA_PROCESSING_COMPLETED("Данные успешно обработаны"),
    SAVE_DATA_TO_FILE_STARTED("Сохранение результатов в файл result.txt..."),
    SAVE_DATA_TO_FILE_COMPLETED("Результаты успешно сохранены"),
    SAVE_DATA_TO_FILE_ERROR("При сохранении результатов возникла ошибка"),
    INPUT_PARAM_IS_NOT_VALID("Входные параметры не валидны");
    private String text;
    Message(String text){
        this.text = text;
    }
    public String getText(){ return text;}
}
