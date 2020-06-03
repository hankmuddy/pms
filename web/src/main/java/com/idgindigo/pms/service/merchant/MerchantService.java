package com.idgindigo.pms.service.merchant;

import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author vomel
 * @since 09.07.14 15:43
 */
@Service
public class MerchantService {

    /*
    $p_OrderId	String [20]	Номер заказа	'8'
    $p_OrderDate	String [12]	Дата заказа	'140214004905' (yyMMddHHmmss)
    $p_SaleDetal	Array of String [256]	Описание товара	'1 х GPS'
    $p_SalePrice	Array of String [256]	Цена за товар	'2.87'
    $p_SaleCurr	String [3]	Валюта	'UAH'
    $p_SaleSum	String [256]	Общая сумма	'0.01'
    $p_ReturnUrl	String [256]	Адрес возврата	'http://fidobank.ua'
    $p_MerchantId	String [8]	Код продавца	'1753693'
    $p_TerminalId	String [8]	Код терминала	'E1762687'
    $p_hashKey	String [256]	Ключ продавца	'FidoBank_send'
    $signature	String [40]	Контрольная сумма	'384c04…742'

    */
    //Функция хеширования по алгоритму
    public String gen_HashCode(String OrderId,
                               String OrderDate,
                               String[] SaleDetal,
                               String[] SalePrice,
                               String SaleCurr,
                               String SaleSum,
                               String ReturnUrl,
                               String MerchantId,
                               String TerminalId,
                               String hashKey) {
        String tmpStr = "";
        if (SalePrice != null) {
            for (int i = 0; i < SaleDetal.length; i++) {
                tmpStr += mb_strlen(SaleDetal[i], "utf8") + "_" + SaleDetal[i];
                tmpStr += mb_strlen(SalePrice[i], "utf8") + "_" + SalePrice[i];
            }
        }
        String resMsg = "";
        if (SaleCurr == null) resMsg += "[валюта] ";
        tmpStr += mb_strlen(SaleCurr, "utf8") + "_" + SaleCurr;
        if (SaleSum == null) resMsg += "[сумма] ";
        tmpStr += mb_strlen(SaleSum, "utf8") + "_" + SaleSum;
        if (OrderId == null) resMsg += "[номер заказа] ";
        tmpStr += mb_strlen(OrderId, "utf8") + "_" + OrderId;
        if (OrderDate == null) resMsg += "[дата заказа] ";
        tmpStr += mb_strlen(OrderDate, "utf8") + "_" + OrderDate;
        if (MerchantId == null) resMsg += "[код продавца] ";
        tmpStr += mb_strlen(MerchantId, "utf8") + "_" + MerchantId;
        if (TerminalId == null) resMsg += "[код терминала] ";
        tmpStr += mb_strlen(TerminalId, "utf8") + "_" + TerminalId;
        if (hashKey == null) resMsg += "[ключ продавца]";
        String str;
        str = mb_strlen(tmpStr, "utf8") > 3000 ? substr(tmpStr, 0, 3000) : tmpStr;
        if (resMsg != null) {
            resMsg = "ERR: Не указаны параметры: " + resMsg;
            System.out.println("resMsg = " + resMsg);
        }
        resMsg = hash_hmac(str, hashKey);
        return resMsg;
    }

    public static String hash_hmac(String value, String key) {
        try {
            // Get an hmac_sha1 key from the raw key bytes
            byte[] keyBytes = key.getBytes();
            SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA1");

            // Get an hmac_sha1 Mac instance and initialize with the signing key
            Mac mac = Mac.getInstance("HmacSHA1");
            mac.init(signingKey);

            // Compute the hmac on input data bytes
            byte[] rawHmac = mac.doFinal(value.getBytes());

            // Convert raw bytes to Hex
            byte[] hexBytes = new Hex().encode(rawHmac);

            //  Covert array of Hex bytes to a String
            return new String(hexBytes, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static int mb_strlen(String string, String encoding) {
        return string.length();
//        return string.getBytes(Charset.forName("UTF-8")).length;
    }

    public static String substr(String string, int start, int end) {
        return string.substring(start, end);
    }

    /*<?php
//Переменные
$p_OrderId = 'Номер заказа/Уникальный код операции';
$p_OrderDate = 'Дата заказа';
$p_SaleDetal = array( //Описание товара
  'Описание 1',
  'Описание 2',
  'Описание n'
);
$p_SalePrice = array( //Цена за товар
  '0.00',
  '0.00',
  '0.00'
);
$p_SaleCurr = 'Валюта (UAH)';
$p_SaleSum = 'Общая сумма';
$p_ReturnUrl = 'Адрес возврата';
$p_MerchantId = 'Код продавца';
$p_TerminalId = 'Код терминала';
$p_hashKey = 'Ключ подписи запроса';
$signature = gen_HashCode($p_OrderId,$p_OrderDate,$p_SaleDetal,$p_SalePrice,$p_SaleCurr,$p_SaleSum,$p_ReturnUrl,$p_MerchantId,$p_TerminalId,$p_hashKey);

//Функция хеширования по алгоритму
function gen_HashCode ( $f_OrderId,
                        $f_OrderDate,
                        $f_SaleDetal,
                        $f_SalePrice,
                        $f_SaleCurr,
                        $f_SaleSum,
                        $f_ReturnUrl,
                        $f_MerchantId,
                        $f_TerminalId,
                        $f_hashKey) {
    $str = '';
    $tmpStr = '';
    $resMsg = null;
    if ($f_SalePrice !== null)
        for ($i = 0; $i < count($f_SaleDetal); $i++) {
            $tmpStr .= mb_strlen($f_SaleDetal[$i] ,'utf8').'_'.$f_SaleDetal[$i];
            $tmpStr .= mb_strlen($f_SalePrice[$i] ,'utf8').'_'.$f_SalePrice[$i];
        }
    if ($f_SaleCurr == null) $resMsg .= '[валюта] ';
    $tmpStr .= mb_strlen($f_SaleCurr ,'utf8').'_'.$f_SaleCurr;
    if ($f_SaleSum == null) $resMsg .= '[сумма] ';
    $tmpStr .= mb_strlen($f_SaleSum ,'utf8').'_'.$f_SaleSum;
    if ($f_OrderId == null) $resMsg .= '[номер заказа] ';
    $tmpStr .= mb_strlen($f_OrderId ,'utf8').'_'.$f_OrderId;
    if ($f_OrderDate == null) $resMsg .= '[дата заказа] ';
    $tmpStr .= mb_strlen($f_OrderDate ,'utf8').'_'.$f_OrderDate;
    if ($f_MerchantId == null) $resMsg .= '[код продавца] ';
    $tmpStr .= mb_strlen($f_MerchantId ,'utf8').'_'.$f_MerchantId;
    if ($f_TerminalId == null) $resMsg .= '[код терминала] ';
    $tmpStr .= mb_strlen($f_TerminalId ,'utf8').'_'.$f_TerminalId;
    if ($f_hashKey == null) $resMsg .= '[ключ продавца]';
    if (mb_strlen($tmpStr ,'utf8')>3000)
        $str = substr($tmpStr,0,3000);
    else
        $str = $tmpStr;
    if ($resMsg !== null) {
        $resMsg = 'ERR: Не указаны параметры: '.$resMsg;
        echo "$resMsg <br>";
    }
    $resMsg = hash_hmac(sha1, $str,$f_hashKey);
    return $resMsg;
}

//Форма передаваемых данных с кнопкой
echo '<form name="PayToFido" action=" https://pay.fidobank.ua/EquPlace/pay " metod="POST">';
echo '<input type="hidden" name="OrderID" value="'.$p_OrderId.'">';
echo '<input type="hidden" name="OrderDate" value="'.$p_OrderDate.'">';
for ($i = 0; $i < count($p_SaleDetal); $i++) {
    echo '<input type="hidden" name="SaleDetal" value="'. $p_SaleDetal[$i].'">';
    echo '<input type="hidden" name="SalePrice" value="'.$p_SalePrice[$i].'">';
}
echo '<input type="hidden" name="SaleCurr" value="'.$p_SaleCurr.'">';
echo '<input type="hidden" name="SaleSum" value="'.$p_SaleSum.'">';
echo '<input type="hidden" name="ReturnUrl" value="'.$p_ReturnUrl.'">';
echo '<input type="hidden" name="HashCode" value="'.$signature.'">';
echo '<button type="submit">Оплатить</button>';
echo '<input type="hidden" name="MerchantId" value="'.$p_MerchantId.'">';
echo '<input type="hidden" name="TerminalId" value="'.$p_TerminalId.'">';
echo '<form/>';
?>
*/
    public enum Status {
        AUTHORIZED(000),//Сделка авторизована;
        NOT_ALLOWED(105),//Транзакция не разрешена банком -эмитентом;
        INSUFFICIENT_FUNDS(116),//Недостаточно средств;
        UNKNOWN_CARD(111),//Несуществующая карта;
        CARD_LOST_OR_STOLEN(108),//Карта утеряна или украдена;
        INCORRECT_TERM(101),//Неверный срок действия карты;
        LIMIT_EXCEEDED(130),//Превышен допустимый лимит расходов;
        EMITTER_UNREACHABLE(290),//Банк-издатель недоступен;
        TECHNICAL_PROBLEM(291);//Техническая или коммуникационная проблема;
        private final int code;

        Status(int code) {
            this.code = code;
        }

        public static Status valueOf(int code) {
            for (Status status : values()) {
                if (status.code == code) return status;
            }
            return null;
        }
    }
}
