<template>
    <div>
        {{errorMessage}}
    </div>
</template>

<script>
    export default {
        name: "errorToast",

        props: {
            errorMessage: String
        }
    }
</script>