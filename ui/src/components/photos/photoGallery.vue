<template>

    <div>
        <div> Hello World this is Photo time!</div>
        <b-button v-b-modal.modalAddPhoto class="btn btn-info btn-lg" >Add Photo</b-button>
        <b-modal id="modalAddPhoto" hide-footer centered title="Add Photo">
            <template slot="modal-title"><h2>Add Photo</h2></template>
            <photoUploader></photoUploader>
        </b-modal>
    </div>


</template>

<script>
    import photoUploader from "../photos/photoUploader"
    export default {
        name: "photoGallery",
        data: function() {
            return {
            }
        },
        components: {
            photoUploader
        },
        methods: {
        }
    }

</script>

<style scoped>

</style>

