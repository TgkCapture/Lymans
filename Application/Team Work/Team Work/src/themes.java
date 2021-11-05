public class themes {

    private String mainColor = "#333";
    private String middleColor = "#444";
    private String textColor = "white";
    private String topColor = "Snow";

    public themes(String color) {
        if (color.equals("Red")) {
            this.mainColor = "rgb(168, 55, 78)";
            this.middleColor = "rgb(206, 70, 97)";
            this.textColor = "white";
            this.topColor = "rgb(250, 209, 217)";
        }
        if (color.equals("Blue")) {
            this.mainColor = "rgb(20, 90, 160)";
            this.middleColor = "rgb(4, 132, 175);";
            this.textColor = "white";
            this.topColor = "skyblue";
        }
        if (color.equals("Indigo")) {
            this.mainColor = "rgb(101, 5, 170)";
            this.middleColor = "rgb(125, 22, 199)";
            this.textColor = "white";
            this.topColor = "rgb(205, 145, 248)";
        }
        if (color.equals("Green")) {
            this.mainColor = "rgb(3, 116, 116)";
            this.middleColor = "darkcyan";
            this.textColor = "white";
            this.topColor = "rgb(161, 252, 252)";
        }
        if (color.equals("Pink")) {
            this.mainColor = "rgb(133, 14, 77)";
            this.middleColor = "rgb(173, 20, 102)";
            this.textColor = "white";
            this.topColor = "rgb(252, 168, 213)";
        }
        if (color.equals("White")) {
            this.mainColor = "whitesmoke";
            this.middleColor = "snow";
            this.textColor = "Black";
            this.topColor = "snow";
        }
        if (color.equals("Orange")) {
            this.mainColor = "rgb(90, 50, 0)";
            this.middleColor = "rgb(138, 76, 0)";
            this.textColor = "white";
            this.topColor = "rgb(255, 218, 172)";
        }
    }

    public String mainColouring() {
        return this.mainColor;
    }

    public String middleColouring() {
        return this.middleColor;
    }

    public String textColouring() {
        return this.textColor;
    }

    public String topColouring() {
        return this.topColor;
    }
}
