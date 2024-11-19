package by.nulleviy.servlet.currencyServlets;

import by.nulleviy.dto.CurrencyDto;
import by.nulleviy.entity.Currency;
import by.nulleviy.entity.exception.CustomException;
import by.nulleviy.entity.exception.ErrorResponse;
import by.nulleviy.service.CurrencyService;

import java.sql.SQLException;
import java.util.List;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.postgresql.util.PSQLException;

import java.io.IOException;


@WebServlet(urlPatterns = "/currencies")
public class CurrenciesServlet extends HttpServlet {
    private final CurrencyService currencyService = new CurrencyService();
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException {
        try {
            List<CurrencyDto> currencyDtoList = currencyService.findAll();
            objectMapper.writeValue(resp.getWriter(), currencyDtoList);

        } catch (CustomException e) {
            resp.setStatus(500);
            objectMapper.writeValue(resp.getWriter(), new CustomException("Чето с гетом"));

        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CurrencyDto currencyDto = objectMapper.readValue(req.getReader(), CurrencyDto.class);
        if(currencyDto.getId() == null
                || currencyDto.getCode() == null
                || currencyDto.getFullName() == null
                || currencyDto.getSign() == null) {
            resp.setStatus(400);
            objectMapper.writeValue(resp.getWriter(), new ErrorResponse(resp.getStatus(), "Отсутствует нужное поле формы"));
        }
        CurrencyDto currency = new CurrencyDto(currencyDto.getId(),currencyDto.getCode(), currencyDto.getFullName(),currencyDto.getSign());
        currencyService.save(currency);
        objectMapper.writeValue(resp.getWriter(), currency);
    }

}
