package com.TourAccount.model;

/**
 * User: User
 * Date: 03.10.14
 * Time: 10:28
 */
public class TourEnum {
    public enum PlotType {
        BAR(0),//гистограмма
        LINE(1);
        public int value;

        private PlotType(int value) {
            this.value = value;
        }
    }

    public enum PlotDataType {
        TOURS(0),//по турам
        TOURITEMS(1); // по данным одного тура
        public int value;

        private PlotDataType(int value) {
            this.value = value;
        }
    }

    public enum TourItemType {
        OUTGOING(0),//расходы по турам
        INCOMING(1), //приходы по турам
        ALL(-1);//И расход и приход
        public int value;

        private TourItemType(int value) {
            this.value = value;
        }

    }

    public interface EditITEM {
        String EDIT_ITEM = "EDIT_ITEM";
        String CREATE_ITEM = "CREATE_ITEM";
    }

    public enum TourArchiveType {
        ACTUAL(0),//актуальные туры
        ARCHIVE(1), //архивные туры
        ALL(-1);//все туры
        public int value;

        private TourArchiveType(int value) {
            this.value = value;
        }

    }
}
