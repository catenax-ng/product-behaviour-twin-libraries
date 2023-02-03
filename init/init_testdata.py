import requests
from requests.auth import HTTPBasicAuth
from pathlib import Path

if __name__ == "__main__":
    #user = "hiuser"
    #password = "qwereroiuzuioiz"
    user = "user"
    password = "pass"

    for i in range(1, 63):
        requestbody = Path('C:/pc/CatenaX/Catena-X_Vehicle_Health_App/exported/testdata_export_20230127-123128_' + str(i) + '_of_63.json').read_text()

        # response = requests.post("https://bmw-provider.dev.demo.catena-x.net/api/rawdata/init/appendbyfile",
        response = requests.post("http://localhost:25551/api/rawdata/init/appendbyfile",
                                 data=requestbody, auth=HTTPBasicAuth(user, password), headers={})

        if response.status_code==200:
            print('ok')
        else:
            print(str(response.status_code) + ": " + response.text)
