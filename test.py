import sys;

def main(stockId, predictDate):
	print (stockId + ' value at ' + predictDate)
	print (123.44)
	print (156.32)

if __name__ == '__main__':
    stockId = sys.argv[1]
    predictDate = sys.argv[2]
    main(stockId, predictDate)