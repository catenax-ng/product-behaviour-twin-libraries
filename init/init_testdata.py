import requests
from requests.auth import HTTPBasicAuth
from pathlib import Path

if __name__ == "__main__":
    locally = False
    
    filename_prefix = 'C:/pc/CatenaX/Catena-X_Vehicle_Health_App/exported/testdata_export_20230206-113234_'
    filecount = 72

    if locally:
        user = "user"
        password = "pass"

        for i in range(1, filecount + 1):
            requestbody = Path(filename_prefix + str(i) + '_of_' + str(filecount) + '.json').read_text()

            response = requests.post("http://localhost:25551/api/rawdata/init/appendbyfile",
                                     data=requestbody, auth=HTTPBasicAuth(user, password), headers={})

            if response.status_code==200:
                print('ok')
            else:
                print(str(response.status_code) + ": " + response.text)
    else:
        user = "hiuser"
        password = "qwereroiuzuioiz"

        for i in range(1, filecount + 1):
            requestbody = Path(filename_prefix + str(i) + '_of_' + str(filecount) + '.json').read_text()

            response = requests.post("https://bmw-provider.dev.demo.catena-x.net/api/rawdata/init/appendbyfile",
                                     data=requestbody, auth=HTTPBasicAuth(user, password), headers={})

            if response.status_code==200:
                print('ok')
            else:
                print(str(response.status_code) + ": " + response.text)