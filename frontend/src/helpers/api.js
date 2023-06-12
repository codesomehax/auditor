import axios from 'axios'
import jwt_decode from 'jwt-decode'

let mode = 'localhost'


if (mode == 'localhost') {
    axios.defaults.baseURL = 'http://localhost:8080/'
}
else if (mode == 'heroku') {
    axios.defaults.baseURL
}

import qs from 'qs'

export function post(_this, url, payload, successCallback, errorCallback, headers = {}) {
    Object.assign(headers, { "Authorization": `Bearer ${localStorage.getItem('jwt')}` })
    return axios({
        withCredentials: true,
        method: 'POST',
        url: url,
        data: payload,
        headers: headers
    }).then(response => {
        successCallback(response)
    }).catch(error => {
        if (!error.status)
            console.log('network error')
        console.log(error.response)
        if (errorCallback)
            errorCallback(error)
    })
}

export function get(_this, url, payload, successCallback, errorCallback) {
    let headers = { "Authorization": `Bearer ${localStorage.getItem('jwt')}` }
    return axios({
        withCredentials: true,
        method: 'GET',
        url: url,
        params: payload.params,
        headers: headers,
        paramsSerializer: params => {
            return qs.stringify(params, { arrayFormat: "repeat" })
        },
    }).then(response => {
        successCallback(response)
    }).catch(error => {
        if (errorCallback)
            errorCallback(error)
    })
}


export function del(_this, url, payload, successCallback, errorCallback) {
    let headers = { "Authorization": `Bearer ${localStorage.getItem('jwt')}` }

    return axios({
        method: 'DELETE',
        url: url,
        headers: headers,
        payload: payload
    }).then(response => {
        successCallback(response)
    }).catch(error => {
        if (errorCallback)
            errorCallback(error)
    })


}

export function download(_this, url, payload, successCallback, errorCallback) {
    let headers = { "Authorization": `Bearer ${localStorage.getItem('jwt')}` }
    axios({
        method: 'get',
        headers: headers,
        url: url,
        responseType: 'arraybuffer'
    })
        .then(response => {

            forceFileDownload(response)
            successCallback(response)

        })
        .catch(error => {
            if (errorCallback) {
                errorCallback(error)
                console.log(error)
            }
        })
}

function forceFileDownload(response) {

    let pattern = /"(.*?[^\\])"/
    let contentDisposition = response.headers['content-disposition']
    let match = contentDisposition.match(pattern)
    let filename = match[1]

    const url = window.URL.createObjectURL(new Blob([response.data]))
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', filename) //or any other extension
    document.body.appendChild(link)
    link.click()
}

export function login(payload, successCallback, errorCallback) {
    let headers = ''

    return axios({
        method: 'POST',
        url: 'authenticate',
        data: payload,
        headers: headers
    }).then(response => {
        localStorage.setItem('jwt', response.data.token)
        localStorage.setItem('user', JSON.stringify(jwt_decode(response.data.token).authorities))
        axios.defaults.headers.common['Authorization'] = `Bearer ${response.data.token}`
        successCallback(response)
    }).catch(error => {
        if (errorCallback) {
            errorCallback(error)
            console.log(error)
        }
    })
}
export function logout(_this, successCallback, errorCallback) {
    localStorage.removeItem('jwt')
    localStorage.removeItem('user')
}
