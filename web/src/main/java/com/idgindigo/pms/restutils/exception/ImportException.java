package com.idgindigo.pms.restutils.exception;

/**
 * @author valentyn_vakatsiienko
 * @since 5/21/14 2:13 PM
 */
public class ImportException extends RestFriendlyException {
    public static final String IMPORT = "import.";
    public static final String UNSUPPORTED_FILE_TYPE = IMPORT + "unsupportedFileType";
    public static final String INVALID_MAPPING = IMPORT + "invalidMapping";
    public static final String INVALID_DATE_FORMAT = IMPORT + "invalidDateFormat";
    public static final String FILE_CORRUPTED = IMPORT + "fileCanNotBeRead";

    public ImportException(String code) {
        super(code);
    }

    public ImportException(String code, String source) {
        super(code, source);
    }
}
