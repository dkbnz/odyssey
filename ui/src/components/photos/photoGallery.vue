<template>
    <div class="containerWithNav">
        <h1 class="page_title">Personal Media</h1>
        <p class="page_title"><i>Here are your photos</i></p>
        <b-button class="btn btn-info" v-b-modal.modalAddPhoto>Add Photo</b-button>

        <div> Hello World this is Photo time!</div>

        <b-modal centered hide-footer id="modalAddPhoto" title="Add Photo">
            <template slot="modal-title"><h2>Add Photo</h2></template>
            <photoUploader></photoUploader>
        </b-modal>
    </div>
</template>

<script>
    import photoUploader from "../photos/photoUploader"

    export default {
        name: "photoGallery",
        data: function () {
            return {}
        },
        components: {
            photoUploader
        },
        methods: {}
    }

</script>

<style scoped>

</style>

