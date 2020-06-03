package com.idgindigo.pms.web.controller.pms;

import com.idgindigo.pms.service.merchant.MerchantService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.inject.Inject;

import static com.idgindigo.pms.service.merchant.MerchantService.hash_hmac;
import static com.idgindigo.pms.service.merchant.MerchantService.mb_strlen;

/**
 * @author vomel
 * @since 09.07.14 16:51
 */
@Controller
@RequestMapping(MerchantController.URL)
public class MerchantController {
    public static final String URL = "/merchant";
    @Inject
    private MerchantService merchantService;
    @RequestMapping(value = "catch", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void catchResponse(
            @RequestParam("ResultMsg") String ResultMsg,
            @RequestParam("ResultComm") String ResultComm,
            @RequestParam("ResultCode") String ResultCode,
            @RequestParam("OrderID") String OrderId,
            @RequestParam("MerchantId") String MerchantId,
            @RequestParam("HashOut") String HashOut,
            @RequestParam("HashKeyResult") String HashKeyResult
    ) {
        System.out.println("OrderID = " + OrderId);
        System.out.println("ResultComm = " + ResultComm);
        System.out.println("ResultMsg = " + ResultMsg);
        String tmpStr = "";
        String resMsg = "";
        tmpStr += mb_strlen(ResultMsg, "utf8") + ResultMsg;
        tmpStr += mb_strlen(ResultComm, "utf8") + ResultComm;
        tmpStr += mb_strlen(ResultCode, "utf8") + ResultCode;
        tmpStr += mb_strlen(OrderId, "utf8") + OrderId;
        tmpStr += mb_strlen(MerchantId, "utf8") + MerchantId;
        String signature = hash_hmac(tmpStr, HashKeyResult);
        if (!signature.equals(HashOut))
            resMsg = "Сигнатура не совпадает. Результат платежа может быть фальшивым.";
        else
            resMsg = "Сигнатура совпадает. Все в порядке.";
        System.out.println("resMsg = " + resMsg);
    }

    @RequestMapping
    public String initialForm(@RequestParam String key, Model model) {
        //payment form view
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderID("121263");
        orderRequest.setOrderDate("180614171005");
        orderRequest.setSaleDetal(new String[]{
                "Описание 1",
                "Описание 2",
                "Описание n"});
        orderRequest.setSalePrice(new String[]{
                "2.00",
                "1.5.00",
                "1.00"});
/*
        orderRequest.setSaleItems(new SaleItem[]{
                new SaleItem("Описание 1", "2.00"),
                new SaleItem("Описание 2", "1.5.00"),
                new SaleItem("Описание n", "1.00")});
*/
        orderRequest.setSaleCurr("UAH");
        orderRequest.setSaleSum("4.5");
        orderRequest.setMerchantId("8810186");
        orderRequest.setTerminalId("E1762762");
        orderRequest.setHashKey("ProdFidoBank@EquPlace2014");//getSaleDetal(orderRequest.getSaleItems()),getSalePrice(orderRequest.getSaleItems())
        int paymentId = 223322;//get real one from db
        orderRequest.setReturnUrl("http://pmscloud.com/return.php");
//        orderRequest.setReturnUrl("http://pmscloud.com/app/merchant/catch?hotelId=" + SecurityUtils.getHotel().getTenantId() + "&paymentId=" + paymentId);
        orderRequest.setSignature(merchantService.gen_HashCode(orderRequest.OrderID, orderRequest.OrderDate, orderRequest.SaleDetal, orderRequest.SalePrice, orderRequest.SaleCurr, orderRequest.SaleSum, orderRequest.ReturnUrl, orderRequest.MerchantId, orderRequest.TerminalId, orderRequest.hashKey));
        model.addAttribute("command", orderRequest);
        return "merchant";
    }

/*
    private String[] getSaleDetal(SaleItem[] saleItems) {
        String[] result = new String[saleItems.length];
        for (int i = 0, saleItemsLength = saleItems.length; i < saleItemsLength; i++) {
            result[i] = saleItems[i].getSaleDetal();
        }
        return result;
    }

    private String[] getSalePrice(SaleItem[] saleItems) {
        String[] result = new String[saleItems.length];
        for (int i = 0, saleItemsLength = saleItems.length; i < saleItemsLength; i++) {
            result[i] = saleItems[i].getSalePrice();
        }
        return result;
    }
*/

    /*    $ResultMsg = $_POST['ResultMsg']; //Сообщение
    $ResultComm = $_POST['ResultComm']; //Результат
    $ResultCode = $_POST['ResultCode']; //Код результата
    $OrderId = $_POST['OrderID']; //Номер заказа
    $MerchantId = $_POST['MerchantId']; //Код продавца
    $HashOut = $_POST['HashOut']; //Контрольная сумма
    $HashKeyResult = 'PmsCloud1234'; //Ключ подписи ответа
    echo "Заказ №$OrderId<br>";
    echo "$ResultComm<br>";
    echo "$ResultMsg<br>";
    $tmpStr = '';
    $resMsg = '';
    $Signature = '';
    $tmpStr .= mb_strlen($ResultMsg,'utf8').$ResultMsg;
    $tmpStr .= mb_strlen($ResultComm,'utf8').$ResultComm;
    $tmpStr .= mb_strlen($ResultCode,'utf8').$ResultCode;
    $tmpStr .= mb_strlen($OrderId,'utf8').$OrderId;
    $tmpStr .= mb_strlen($MerchantId,'utf8').$MerchantId;
    $Signature = hash_hmac(sha1,$tmpStr,$HashKeyResult);
    if ($Signature !== $HashOut)
    $resMsg = 'Сигнатура не совпадает. Результат платежа может быть фальшивым.';
    else
    $resMsg = 'Сигнатура совпадает. Все в порядке.';

    $f=fopen('respond.txt','a');
    fwrite($f, $ResultMsg."|".$ResultComm.'|'.$ResultCode.'|'.$OrderId."|".$resMsg);
    fclose($f);
    echo "good";*/

    @Getter
    @Setter
    @ToString
    public static class OrderRequest {
        private String OrderID;//	String [20]	Номер заказа	'8'
        private String OrderDate;//	String [12]	Дата заказа	'140214004905' (yyMMddHHmmss)
        //        private SaleItem[] SaleItems;
        private String SaleCurr;//	String [3]	Валюта	'UAH'
        private String SaleSum;//	String [256]	Общая сумма	'0.01'
        private String ReturnUrl;//	String [256]	Адрес возврата	'http://fidobank.ua'
        private String MerchantId;//	String [8]	Код продавца	'1753693'
        private String TerminalId;//	String [8]	Код терминала	'E1762687'
        private String hashKey;//	String [256]	Ключ продавца	'FidoBank_send'
        private String signature;//	String [40]	Контрольная сумма	'384c04…742'
        private String[] SaleDetal;
        private String[] SalePrice;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SaleItem {
        private String saleDetal;//	Array of String [256]	Описание товара	'1 х GPS'
        private String salePrice;//	Array of String [256]	Цена за товар	'2.87'

    }
}
