<template>
    <div class="vue-play">
        <nav>
            <b-navbar-brand href="#">NavBar</b-navbar-brand>
            <a class="java" href="https://java.com/en/">
                <img width="450" height="300"  :src="logos.javaLogo" alt="Java Logo" />
            </a>
            <a class="play" href="https://www.playframework.com/">
                <img width="400" height="400" :src="logos.playLogo" alt="Play Framework Logo" />
            </a>
            <a class="vue" href="https://vuejs.org/">
                <img class="App-logo" width="400" height="400" :src="logos.vueLogo" alt="Vue Logo" />
            </a>
        </nav>
    </div>
</template>
<script>
    import assets from '../assets'
    export default {
        computed: {
            logos () {
                return assets
            }
        },
        data () {
            return {
                items: [
                    {age: 40, first_name: 'Dickerson', last_name: 'Macdonald'},
                    {age: 21, first_name: 'Larsen', last_name: 'Shaw'},
                    {age: 89, first_name: 'Geneva', last_name: 'Wilson'},
                    {age: 38, first_name: 'Jami', last_name: 'Carney'}
                ]
            }
        }
    }
</script>
