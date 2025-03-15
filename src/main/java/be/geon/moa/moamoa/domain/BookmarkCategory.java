package be.geon.moa.moamoa.domain;

public enum BookmarkCategory {
    ARTICLE("기사"),
    BLOG("블로그"),
    VIDEO("동영상"),
    DOCUMENT("문서"),
    TUTORIAL("튜토리얼"),
    REFERENCE("참조자료"),
    OTHER("기타");

    private final String displayName;

    BookmarkCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static BookmarkCategory fromDisplayName(String displayName) {
        for (BookmarkCategory category : values()) {
            if (category.displayName.equals(displayName)) {
                return category;
            }
        }
        return null;
    }
}