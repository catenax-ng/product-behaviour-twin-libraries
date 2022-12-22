import requests
from requests.auth import HTTPBasicAuth
from pathlib import Path

if __name__ == "__main__":
    user = "hiuser"
    password = ""

    for i in range(1, 167):
        requestbody = Path('testdata_export_20221129-130232_' + str(i) + '_of_167.json').read_text()

        response = requests.post("https://bmw-provider.dev.demo.catena-x.net/api/rawdata/init/appendbyfile",
                                 data=requestbody, auth=HTTPBasicAuth(user, password), headers={})

        if response.status_code==200:
            print('ok')
        else:
            print(str(response.status_code) + ": " + response.text)
