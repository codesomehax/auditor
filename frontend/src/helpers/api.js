import axios from 'axios'
axios.defaults.baseURL = 'https://senior-project-nu.herokuapp.com';
import qs from 'qs'

export function post(_this, url, payload, successCallback, errorCallback, headers = '') {

    return axios({
        method: 'POST',
        url: url,
        data: payload,
        headers: headers
    }).then(response => {
        successCallback( response );
    }).catch(error => {
        if(!error.status)
            console.log('network error');
        console.log(error.response);
        if(errorCallback)
            errorCallback( error );
    });
}

export function get(_this, url, payload, successCallback, errorCallback) {
    let headers = '';

    return axios({
        method: 'GET',
        url: url,
        params: payload.params,
        headers: headers,
        paramsSerializer: params => {
            return qs.stringify(params, { arrayFormat: "repeat" })
        },
    }).then(response => {
        successCallback( response );
    }).catch(error => {
        if(errorCallback)
            errorCallback( error );
    });
}


export function del(_this, url, payload, successCallback, errorCallback) {
    let headers = '';

    return axios({
        method: 'DELETE',
        url: url,
        headers: headers,
        payload: payload
    }).then(response => {
        successCallback( response );
    }).catch(error => {
        if(errorCallback)
            errorCallback( error );
    });


}