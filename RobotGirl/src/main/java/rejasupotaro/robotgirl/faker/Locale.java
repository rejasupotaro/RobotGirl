package rejasupotaro.robotgirl.faker;

public enum Locale {
    JA("ja.yml");

    private String mFileName;

    public String getFileName() {
        return mFileName;
    }

    private Locale(String fileName) {
        mFileName = fileName;
    }
}
