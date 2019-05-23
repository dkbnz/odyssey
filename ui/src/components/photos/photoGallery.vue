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
            <tr v-for="rowNumber in (amountOfRows)">
                <td v-for="photo in getRowPhotos(rowNumber)">
                    <b-img :src="photo.imgUrl" thumbnail></b-img>
                </td>
            </tr>
        </table>
        <b-pagination
                v-model="currentPage"
                :total-rows="rows"
                :per-page="perPage"
        ></b-pagination>
    </div>
</template>

<script>
    import photoUploader from "../photos/photoUploader"

    export default {
        name: "photoGallery",
        data: function () {
            return {
                photos: [],
                rowSize: 2,
                amountOfRows: 1,
                currentPage: 0,
            }
        },
        computed: {
            rows() {
                return this.photos.length
            },
            perPage() {
                return this.rowSize * this.amountOfRows
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
            /**
             * Creates a POST request to upload photo(s) to the backend.
             * @param files     The photo(s) uploaded from the personal photos component.
             */
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
            /**
             * Creates the form data to send as the body of the POST request to the backend.
             * @param files             The photo(s) uploaded from the personal photos component.
             * @returns {FormData}      The FormData stringified for use in the POST request.
             */
            getFormData(files) {
                let personalPhotos = new FormData();
                for (let i=0; i < files.length; i++) {
                    personalPhotos.append('photo' + i, files[i]);
                }
                return personalPhotos;
            },
            /**
             * TODO: Link to the backend then comment.
             * @returns {Promise<Response | never>}
             */
            getPhotos() {
                let self = this;
                return fetch(`/v1/travtypes`, {
                    accept: "application/json"
                })
                    .then(this.parseJSON)
                    .then((data) => self.photos = data);
            },
            /**
             * Retrieves a json body from a response.
             * @param response      The response parsed into json.
             * @returns {*}
             */
            parseJSON(response) {
                return response.json();
            },
            /**
             * Calculates the positions of photos within a gallery grid row.
             * @param rowNumber     The row currently having photos positioned within it.
             */
            getRowPhotos(rowNumber) {
                let numberOfPhotos = (this.photos.length);
                let endRowIndex = ((rowNumber * this.rowSize) + ((this.currentPage - 1) * this.perPage));
                let startRowIndex = (rowNumber - 1) * this.rowSize  + ((this.currentPage - 1) * this.perPage);

                // Check preventing an IndexOutOfRangeError, before filling the row with photos indexed from the list.
                if (endRowIndex > numberOfPhotos) {
                    return this.photos.slice(startRowIndex);
                } else {
                    return this.photos.slice(startRowIndex, endRowIndex);
                }
            }
        }
    }

</script>

<style scoped>

</style>

