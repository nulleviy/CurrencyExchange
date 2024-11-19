package by.nulleviy.dao;

import by.nulleviy.Utils.ConnectionManager;
import by.nulleviy.entity.ExchangeRate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import lombok.SneakyThrows;

public class ExchangeRateDao {
    CurrencyDao currencyDao = new CurrencyDao();

    private final static String FIND_ALL = """
            SELECT * FROM exchange_rates
            """;

    private final static String FIND_BY_ID = FIND_ALL + """
            WHERE id = ?
            """;

    private final static String FIND_BY_CODES = FIND_ALL + """
            JOIN currencies AS base_currency ON exchange_rates.base_currency_id = base_currency.id
            JOIN currencies AS target_currency ON exchange_rates.target_currency_id = target_currency.id
            WHERE base_currency.code = ? AND target_currency.code = ?
            """;

    private final static String SAVE = """
            INSERT INTO exchange_rates (base_currency_id, target_currency_id, rate) VALUES (?,?,?)
            """;

    private final static String UPDATE = """
            UPDATE exchange_rates
            SET rate = ?
            WHERE base_currency_id = ? AND target_currency_id = ?
            """;

    public void update(ExchangeRate exchangeRate){
        try (Connection connection = ConnectionManager.get();
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE)) {

            preparedStatement.setBigDecimal(1,exchangeRate.getRate());
            preparedStatement.setObject(2,exchangeRate.getBaseCurrencyId().getId());
            preparedStatement.setObject(3,exchangeRate.getTargetCurrencyId().getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(ExchangeRate exchangeRate){
        try (Connection connection = ConnectionManager.get();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE)) {

            preparedStatement.setObject(1,exchangeRate.getBaseCurrencyId().getId());
            preparedStatement.setObject(2,exchangeRate.getTargetCurrencyId().getId());
            preparedStatement.setBigDecimal(3,exchangeRate.getRate());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<ExchangeRate> findAll() {
        try (Connection connection = ConnectionManager.get();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_ALL)) {

            ResultSet resultSet = preparedStatement.executeQuery();
            List<ExchangeRate> exchangeRates = new ArrayList<>();

            while(resultSet.next()){
                exchangeRates.add(buildExchangeRate(resultSet));
            }
            return exchangeRates;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SneakyThrows
    public ExchangeRate buildExchangeRate(ResultSet resultSet) {
            return new ExchangeRate(
                resultSet.getInt("id"),
                currencyDao.findById(resultSet.getInt("base_currency_id")),
                currencyDao.findById(resultSet.getInt("target_currency_id")),
                resultSet.getBigDecimal("rate")
            );
    }


    public ExchangeRate findById(int id){
        try (Connection connection = ConnectionManager.get();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID)) {

            preparedStatement.setInt(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            ExchangeRate exchangeRate = null;

            if(resultSet.next()){
               exchangeRate = buildExchangeRate(resultSet);
            }return exchangeRate;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ExchangeRate findByCodes(String firstCode, String secondCode){
        try (Connection connection = ConnectionManager.get();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODES)) {

            ExchangeRate exchangeRate = null;
            preparedStatement.setString(1,firstCode);
            preparedStatement.setString(2,secondCode);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                exchangeRate = buildExchangeRate(resultSet);
            }return exchangeRate;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
