# Conversion Rate API
IntelliJ Java project, see the [full spec](https://github.com/LousyLeonard/conversion-rate-api/blob/master/conversion-rate-api-spec.pdf).

### Quick Start API End-Point Examples
* http://localhost:8080/date?start=2020-01-01&&end=2021-01-02
* http://localhost:8080/convert?date=2021-10-20&currencyFrom=USD&currencyTo=GBP&amount=100
* http://localhost:8080/stats/max?start=2020-01-01&&end=2021-01-02&currency=GBP
* http://localhost:8080/stats/average?start=2020-01-01&&end=2021-01-02&currency=GBP

### Date End Point
Retrieves all exchange rate records for the date range given.
* @param start - The ISO formatted date start date, without time - Optional, if omitted the start of time is used.
* @param end   - The ISO formatted date start date, without time - Optional, if omitted the end of time is used.

Example response:\
[{"date":"2020-03-16","rates":{"CHF":["1.0546"],"HRK":["7.5785"],"MXN":["25.4478"],"LVL":["N/A"]]

### Convert End Point
Converts a given value between two currencies for the exchange rate at the given date.
* @param date         - The ISO formatted date, without time.
* @param currencyFrom - Three character currency identifier.
* @param currencyTo   - Three character currency identifier.
* @param amount       - Pure numerical value to convert.

Example response:\
72.70326077604749

### Stats End Points
Performs the given stat function.
* @param start    - The ISO formatted date start date, without time - Optional, if omitted the start of time is used.
* @param end      - The ISO formatted date start date, without time - Optional, if omitted the end of time is used.
* @param currency - Three character currency identifier.

Example response:\
72.70326077604749

## Discussion
Time ran short, priorities for future development:
* Flesh out Tests.
* Standardise Response types.
* Flatten the CSV database structure.
* Core and Database/Datasource libraries.
* Better documentation
* Better Configuration on the CSV and maybe lazy reading.

Overall I'm not particularly happy with this solution, but I need to put it down as I'm investing too much time into it.\
There's a bit of niggle in the CSV reader code that puts the rates into an array for a single currency type where I would expect a 1:1 relationship, this causes a fair amount of ugliness in the controller code.\
I should really of converted the currency rates into doubles at the CSV import stage to, the N/A values can just be ignored.
Additionally, I would like to provide more consistent and better response types to the calls.

### Assumptions
* Web API
* CSV backend
* CSV Load on server startup, bulking out the start time. Not expecting it to grow.

### Dependencies
See build.gradle for a list.
Dependencies introduced to limit my involvement with the WebAPI side and CSV import, so that I could focus on the key bits highlighting in the spec.
