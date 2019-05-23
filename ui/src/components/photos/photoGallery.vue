<template>
    <div class="containerWithNav">
        <h1 class="page_title">Personal Media</h1>
        <p class="page_title"><i>Here are your photos</i></p>
        <div class="column">
            <div class="row">
                <!--<img v-for="photo in photos" :src="photo.imgUrl" />-->
            </div>
        </div>
        <table>
            <tr v-for="rowNumber in (Math.floor(photos.length/rowSize) + 1)">
                <td v-for="photo in getRowPhotos(rowNumber)">
                    <img :src="photo.imgUrl" width="400px" height="400px" />
                </td>
            </tr>
        </table>
        <div>
            <!--{{photos}}-->
        </div>
        <b-button class="btn btn-info" v-b-modal.modalAddPhoto>Add Photo</b-button>
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
            return {
                photos: [],
                rowSize: 3
            }
        },
        components: {
            photoUploader
        },
        mounted() {
            this.getPhotos()
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
                    self.files = null;
                });
                this.$refs['uploaderModal'].hide()
            },

            getPhotos() {
                let self = this;
                return fetch(`/v1/travtypes`, {
                    accept: "application/json"
                })
                    .then(this.parseJSON)
                    .then((data) => self.photos = data);
            },

            parseJSON(response) {
                return response.json();
            },

            getRowPhotos(rowNumber) {
                let endRow = Math.min((this.photos.length), (rowNumber*this.rowSize));
                if (endRow === this.photos.length) {
                    return this.photos.slice((rowNumber -1 )*this.rowSize);
                } else {
                    return this.photos.slice((rowNumber -1 )*this.rowSize, endRow);
                }
            }
        }
    }

</script>

<style scoped>

</style>

