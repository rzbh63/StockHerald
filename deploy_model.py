
# coding: utf-8

# In[9]:

import os
os.environ['KMP_DUPLICATE_LIB_OK']='True'
import quandl
import pandas as pd
import numpy as np
from nltk.sentiment.vader import SentimentIntensityAnalyzer
from nltk.sentiment.util import *
from sklearn.preprocessing import MinMaxScaler
from keras.models import load_model
from datetime import date, timedelta
from pandas.tseries.offsets import BDay

def stocks(start_date, end_date):
    # use quandl to acquire nasdaq composite
    ndq = quandl.get("NASDAQOMX/COMP-NASDAQ",
                        start_date = start_date, 
                        end_date = end_date)
    ndq_df = ndq.reset_index()
    val = ndq_df['Index Value'].tolist()
    return ndq_df, val

def analyze_sentiment(day_headline_lst):
    sentiments = []
    sid = SentimentIntensityAnalyzer()
    for headline in day_headline_lst:
        lst = []
        if len(headline) >3:
            for each in headline.split('$@$'):
                sentiment = sid.polarity_scores(each)['compound']
                lst.append(sentiment)
            arr_mean = np.mean(lst, axis=0)
        else:
            arr_mean = 0.0
        sentiments.append(arr_mean)
    return sentiments
    
def main(str1, str2, day):
    # filter string len >5
    if len(str1) <= 5:
        today = pd.datetime.today()
        prev_day = today - BDay(30)
        prev_date = prev_day.strftime('%Y-%m-%d')
        today_date = today.strftime('%Y-%m-%d')
        df, price = stocks(prev_date, today_date)
    else:
        price = [float(a) for a in str1.split('|')]
        
    model = load_model('model_15.hdf5')
    data_stock = np.asarray(price).reshape(-1, 1)
    scaler = MinMaxScaler(feature_range=(0, 1))
    min_max_price = scaler.fit_transform(data_stock)
    
    # calculate sentiment
    day_headline_lst = str2.split('|||')
    senti_day = analyze_sentiment(day_headline_lst)
    data_senti = np.asarray(senti_day).reshape(15, -1)
    
    # concate senti and stock price
    data = np.concatenate((data_senti, min_max_price),axis=1)
    test_data = data.reshape(-1, 15, 2)
    
    predict = []
    for i in range(day):
        Xt = model.predict(test_data)
        val = scaler.inverse_transform(Xt)
        price = ''.join([str(price) for i in val for price in i])
        predict.append(price)
        c = np.asarray([[0, float(price)]])
        new_test_data = test_data[:, 1:, :]
        aa = new_test_data.reshape(-1, 2)
        test_1 = np.concatenate((aa, c))
        test_data = test_1.reshape(-1, 15, 2)
    return predict

if __name__ == "__main__":
    try:
        str1 = sys.argv[1]
        headline = sys.argv[2]
        day = sys.argv[3]
        print("received:" + str1)
        print("received:" + headline)
        print("received:" + day)
        
        if str1 == '' or headline == '' or day == '':
            str1 = '7938.69|7723.95|7714.48|7728.97|7838.96|7642.67|7637.54|7691.52|7643.38|7669.17|7729.32|7828.91|7848.69|7895.55|7891.78'
            headline = "Last-minute Christmas rush lifts UK retail sales$@$Guarantee minimum wage for gig economy workers, says Frank Field|||Why the UK economy could fare better in 2017 than forecasters predict$@$US oil stocks surge|||British tourism to Spain hits record$@$BoE's Cunliffe: business investment to remain weak|||British firms suffer Brexit impact, as ECB's Draghi warns on US protectionism – as it happened$@$'It's a crisis that keeps on hurting' - experts debate Brexit watch data|||European markets slip back$@$Bank of England ‘keeping close eye on consumer spending’, says Carney|||Trump poses a risk to global economy, Fitch warns - as it happened$@$UK and US markets outperform Europe|||No justification for IMF to be in Greek deal - ECB's Nowotny$@$UK economy grew 0.7% in three months to January - NIESR|||Eurozone growth rises to 0.5%; Trump adviser claims euro 'grossly undervalued'- as it happened$@$US consumer confidence falls|||Dow hits new peak as Wall Street opens$@$EU commissioner hints EU could go it alone on Greece|||Devil-may-care investors fall under spell of Trump-mania$@$UK growth revised up; Unilever vows to unlock value – as it happened|||European markets edge higher$@$US house sales hit ten year high||||||Unilever hikes profit guidance$@$Unilever vows to 'accelerate delivery of value'|||UK to set out approach to foreign takeovers$@$Business investment suffers first annual fall since 2009|||European markets end lower$@$GDP: Some more charts"
            day = 20
        else:
            day = int(day)
    except:
        str1 = '7938.69|7723.95|7714.48|7728.97|7838.96|7642.67|7637.54|7691.52|7643.38|7669.17|7729.32|7828.91|7848.69|7895.55|7891.78'
        headline = "Last-minute Christmas rush lifts UK retail sales$@$Guarantee minimum wage for gig economy workers, says Frank Field|||Why the UK economy could fare better in 2017 than forecasters predict$@$US oil stocks surge|||British tourism to Spain hits record$@$BoE's Cunliffe: business investment to remain weak|||British firms suffer Brexit impact, as ECB's Draghi warns on US protectionism – as it happened$@$'It's a crisis that keeps on hurting' - experts debate Brexit watch data|||European markets slip back$@$Bank of England ‘keeping close eye on consumer spending’, says Carney|||Trump poses a risk to global economy, Fitch warns - as it happened$@$UK and US markets outperform Europe|||No justification for IMF to be in Greek deal - ECB's Nowotny$@$UK economy grew 0.7% in three months to January - NIESR|||Eurozone growth rises to 0.5%; Trump adviser claims euro 'grossly undervalued'- as it happened$@$US consumer confidence falls|||Dow hits new peak as Wall Street opens$@$EU commissioner hints EU could go it alone on Greece|||Devil-may-care investors fall under spell of Trump-mania$@$UK growth revised up; Unilever vows to unlock value – as it happened|||European markets edge higher$@$US house sales hit ten year high||||||Unilever hikes profit guidance$@$Unilever vows to 'accelerate delivery of value'|||UK to set out approach to foreign takeovers$@$Business investment suffers first annual fall since 2009|||European markets end lower$@$GDP: Some more charts"
        day = 20

    predict = main(str1, headline, day)
    print("prediction finished")
    print(predict)

