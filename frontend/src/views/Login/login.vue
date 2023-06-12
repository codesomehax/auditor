<template>
    <form
        class="center"
        v-on:submit="loginFormHander"
    >
        <h1>Log in</h1>
        <div class="form-group">
            <input
                type="email"
                class="form-control"
                v-model="email"
                aria-describedby="emailHelp"
                placeholder="Enter email"
            >
            <input
                type="password"
                class="form-control"
                v-model="password"
                placeholder="Password"
            >
            <button
                type="submit"
                class="btn primary"
            >Submit</button>
        </div>
    </form>
</template>

<script>
import { login } from '../../helpers/api'

export default {
    data: () => ({
        email: '',
        password: ''
    }),
    methods: {
        loginFormHander(e) {
            e.preventDefault()
            console.log(e)
            login({
                "email": this.email,
                "password": this.password
            }, (resp) => {
                console.log("success", resp)
                this.$router.push({ name: 'Curriculums' })
            }, (err) => {
                console.log("error", err)
            })
        }
    }
}
</script>

<style scoped>
.center {
    height: 60%;
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
}

.form-group {
    display: flex;
    flex-direction: column;
    width: 300px;
}

input,
button {
    border: 1px solid #ccc;
    border-radius: 5px;
    padding: 10px;
    margin: 10px;
}

button {
    color: white
}
</style>
  


  