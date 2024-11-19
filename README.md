# [???](https://www.reddit.com/media?url=https%3A%2F%2Fpreview.redd.it%2F%25D0%25B4%25D0%25B6%25D0%25BE%25D0%25BA%25D0%25B5%25D1%2580%25D0%25B3%25D0%25B5-%25D0%25BD%25D0%25B0%25D1%2587%25D0%25B0%25D0%25BB%25D0%25BE-v0-nohg2vivw5eb1.jpeg%3Fwidth%3D516%26format%3Dpjpg%26auto%3Dwebp%26s%3D5f49f35a007d4dc385c2e8913f55b9646921179c)

# Currencies

#### GET /currencies
   - Возвращает список всех валют. 
 
#### GET /currency/USD
   - Возвращает определенную валюту. Код валюты указан в адресе запроса.

#### POST /currencies
   - Добавление новой валюты в базу данных. Данные передаются в теле запроса в формате x-www-form-urlencoded. Поля формы: name, code, sign. 

# Exchange rates

### GET /exchangeRates
- Возвращает список всех обменных курсов.

### POST /exchangeRates
- Добавление нового курса валют в базу данных. Данные передаются в теле запроса в формате x-www-form-urlencoded. Поля формы: baseCurrencyCode, targetCurrencyCode, rate.

### GET /exchangeRate/USDEUR
- Возвращает определенный обменный курс. Валютная пара указывается последоват ельными кодами валют в адресе запроса.

### PATCH /exchangeRate/USDEUR
- Обновляет существующий обменный курс в базе данных. Валютная пара указывается последовательными кодами валют в адресе запроса. Данные передаются в теле запроса в формате x-www-form-urlencoded. Единственное поле формы — rate.

###### [.](https://yt3.googleusercontent.com/QkQWo5Fc_TnQkMH6kqjGbkDoWldsufHZfXi09MiSfy6VtY_BiCCwMk44tiDQ5ovo9Q4XvskmHEw=s900-c-k-c0x00ffffff-no-rj)
# Currency exchange

#### GET /exchange?from=BASE_CURRENCY_CODE&to=TARGET_CURRENCY_CODE&amount=$AMOUNT
- Рассчитайте конвертацию определенной суммы денег из одной валюты в другую. Валютная пара и сумма указаны в адресе запроса.
# [!!!](https://cs11.livemaster.ru/storage/topicavatar/600x450/37/56/314cbff17f57a5163c60beb7cea7add03b54j8.jpg?h=hEYaDvhj3kCoqqnQu79FiA)