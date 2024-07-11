package fr.destouesse.blelibrary.constants;

public class BleConstantsRecordAccessControlPoint {
    public static final byte RACP_OPCODE_REPORT_STORED_RECORDS     = 0x01;
    public static final byte RACP_OPCODE_DELETE_STORED_RECORDS     = 0x02;
    public static final byte RACP_OPCODE_ABORT_OPERATION           = 0x03;
    public static final byte RACP_OPCODE_REPORT_NUMBER_OF_RECORDS  = 0x04;
    public static final byte RACP_OPCODE_NUMBER_OF_STORED_RESPONSE = 0x05;
    public static final byte RACP_OPCODE_RESPONSE_CODE             = 0x06;
    public static final byte RACP_OPCODE_COMBINED_REPORT           = 0x07;
    public static final byte RACP_OPCODE_COMBINED_REPORT_RESPONSE  = 0x08;

    public static final byte RACP_OPVALUE_ALL_RECORDS              = 0x01;
    public static final byte RACP_OPVALUE_LESS_THAN_OR_EQUAL       = 0x02;
    public static final byte RACP_OPVALUE_GREATER_THAN_OR_EQUAL    = 0x03;
    public static final byte RACP_OPVALUE_WITHIN_RANGE             = 0x04;
    public static final byte RACP_OPVALUE_FIRST_RECORD             = 0x05;
    public static final byte RACP_OPVALUE_LAST_RECORD              = 0x06;

    public static final byte RACP_RESPONSE_SUCCESS                 = 0x01;
    public static final byte RACP_RESPONSE_OPCODE_NOT_SUPPORTED    = 0x02;
    public static final byte RACP_RESPONSE_INVALID_OPERATOR        = 0x03;
    public static final byte RACP_RESPONSE_OPERATOR_NOT_SUPPORTED  = 0x04;
    public static final byte RACP_RESPONSE_INVALID_OPERAND         = 0x05;
    public static final byte RACP_RESPONSE_NO_RECORD_FOUNDS        = 0x06;
    public static final byte RACP_RESPONSE_ABORT_UNSUCCESSFUL      = 0x07;
    public static final byte RACP_RESPONSE_PROCEDURE_NOT_COMPLETED = 0x08;
    public static final byte RACP_RESPONSE_OPERAND_NOT_SUPPORTED   = 0x09;

}

