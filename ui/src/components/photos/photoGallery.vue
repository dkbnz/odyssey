<template>
    <div class="containerWithNav">
        <h1 class="page_title">Personal Media</h1>
        <p class="page_title"><i>Here are your photos</i></p>
        <b-button class="btn btn-info" v-b-modal.modalAddPhoto>Add Photo</b-button>

    </div>
        <div> Hello World this is Photo time!</div>
        <b-modal ref="uploaderModal" id="modalAddPhoto" hide-footer centered title="Add Photo">
            <template slot="modal-title"><h2>Add Photo</h2></template>
            <photoUploader v-on:save-photos="sendPhotosToBackend"></photoUploader>
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
        methods: {
            sendPhotosToBackend: function(files) {
                let self = this;
                console.log(files[0].name);
                fetch(`/v1/savePhotos`, {
                    method: 'POST',
                    headers:{'content-type': 'application/json'},
                    body: JSON.stringify({'photos': files})

                }).then(function(response) {
                    files = null;
                })
                this.$refs['uploaderModal'].hide()
            }
        }
    }

</script>

<style scoped>

</style>

