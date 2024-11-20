package by.nulleviy.servlet.exchangeServlets;

import by.nulleviy.dto.ExchangeRateRequestDto;
import by.nulleviy.entity.exception.CustomException;
import by.nulleviy.entity.exception.ErrorResponse;
import by.nulleviy.service.CurrencyService;
import by.nulleviy.service.ExchangeRateService;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/exchange_rates")
public class ExchangeRatesServlet extends HttpServlet {
    private final ObjectMapper mapper = new ObjectMapper();
    private final CurrencyService currencyService = new CurrencyService();
    private final ExchangeRateService exchangeRateService = new ExchangeRateService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            mapper.writeValue(resp.getWriter(),exchangeRateService.findAll());
        }catch(CustomException e){
            resp.setStatus(500);
            mapper.writeValue(resp.getWriter(),new CustomException("Что-то с гетом"));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ExchangeRateRequestDto exchangeRateRequestDto = mapper.readValue(req.getReader(), ExchangeRateRequestDto.class);
        if(exchangeRateRequestDto!=null){
            if(currencyService.findByCode(exchangeRateRequestDto.getBaseCurrencyId()).isEmpty()
               || currencyService.findByCode(exchangeRateRequestDto.getTargetCurrencyId()).isEmpty()){
                resp.setStatus(404);
                mapper.writeValue(resp.getWriter(), new ErrorResponse(resp.getStatus(),"Одна (или обе) валюты из валютной пары не существует в БД"));
            }
        }
        if(exchangeRateRequestDto.getId()==null|| exchangeRateRequestDto.getBaseCurrencyId()==null
        || exchangeRateRequestDto.getTargetCurrencyId()==null
        || exchangeRateRequestDto.getRate()==null){
            resp.setStatus(400);
            mapper.writeValue(resp.getWriter(),new ErrorResponse(resp.getStatus(),"Отсутствует нужное поле формы"));
        }
        exchangeRateService.save(exchangeRateRequestDto);
    }


    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        ExchangeRateRequestDto exchangeRateRequestDto = mapper.readValue(req.getReader(), ExchangeRateRequestDto.class);
        if(exchangeRateRequestDto.getRate()==null || exchangeRateRequestDto.getBaseCurrencyId()==null || exchangeRateRequestDto.getTargetCurrencyId()==null || exchangeRateRequestDto.getId()==null){
            resp.setStatus(400);
            mapper.writeValue(resp.getWriter(),new ErrorResponse(resp.getStatus(),"Отсутствует нужное поле формы"));
        }
        if(exchangeRateService.findByCodes(exchangeRateRequestDto.getBaseCurrencyId(),exchangeRateRequestDto.getTargetCurrencyId()).isEmpty()){
            resp.setStatus(404);
            mapper.writeValue(resp.getWriter(),new ErrorResponse(resp.getStatus(),"Валютная пара отсутствует в базе данных"));
        }
        exchangeRateService.update(exchangeRateRequestDto);
    }
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getMethod().equalsIgnoreCase("PATCH")){
            this.doPatch(req, resp);
        } else{
            super.service(req, resp);
        }
    }
}
