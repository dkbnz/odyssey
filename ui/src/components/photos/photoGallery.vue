<template>
    <div class="containerWithNav">
        <h1 class="page_title">Personal Media</h1>
        <p class="page_title"><i>Here are your photos</i></p>
        <b-button class="btn btn-info" block v-b-modal.modalAddPhoto>Add Photo</b-button>
        <b-modal ref="uploaderModal" id="modalAddPhoto" hide-footer centered title="Add Photo">
            <template slot="modal-title"><h2>Add Photo</h2></template>
            <photoUploader v-on:save-photos="sendPhotosToBackend"></photoUploader>
        </b-modal>
        <table style="margin-top:20px">
            <tr v-for="rowNumber in (Math.floor(photos.length/rowSize) + 1)">
                <td v-for="photo in getRowPhotos(rowNumber)">
                    <b-img :src="photo.imgUrl" thumbnail></b-img>
                </td>
            </tr>
        </table>
    </div>
</template>

<script>
    import photoUploader from "../photos/photoUploader"

    export default {
        name: "photoGallery",
        data: function () {
            return {
                photos: [],
                rowSize: 3,
            }
        },
        props: {
            profile: Object
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
                fetch(`/v1/photos/` + self.profile.id, {
                    method: 'POST',
                    headers:{'content-type': 'application/json'},
                    body: JSON.stringify(this.getFormData(files))

                }).then(response =>  {
                    if (response.status === 201) {
                        self.files = null;
                    } else {
                        console.log("ERROR");
                    }

                });
                this.$refs['uploaderModal'].hide()
            },

            getFormData(files) {
                let personalPhotos = new FormData();
                for (let i=0; i < files.length; i++) {
                    personalPhotos.append('photo' + i, files[i]);
                }
                return personalPhotos;
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

