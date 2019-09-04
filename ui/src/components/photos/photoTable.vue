<template>
    <div>
        <table ref="gallery" style="margin-top:20px">
            <!--Table containing the rows of photos to be displayed-->
            <tr v-for="rowNumber in (numberOfRows)">
                <td v-for="photo in getRowPhotos(rowNumber)">
                    <b-container class="p-1" :class="{colorBlue: selected(photo)}">
                        <b-img :src="getThumbImage(photo.id)" @click="$emit('photo-click', photo)" @error="imageAlt"
                               alt="Image not Found" thumbnail>
                        </b-img>
                    </b-container>
                    <b-select @change="$emit('privacy-update', photo)" style="width: 100%"
                              :disabled="userProfile.profilePicture !== null
                               && userProfile.profilePicture.id === photo.id"
                              v-if="showDropdown"
                              v-model="photo.public"
                              :class="{colorBlue: userProfile.profilePicture === null
                              || (userProfile.profilePicture !== null
                              && userProfile.profilePicture.id !== photo.id),
                              colorDisabled: (userProfile.profilePicture !== null
                              && userProfile.profilePicture.id === photo.id)}">
                        <option value="true">
                            Public
                        </option>
                        <option value="false">
                            Private
                        </option>
                    </b-select>
                </td>
            </tr>
        </table>
        <b-pagination
                :per-page="perPage"
                :total-rows="rows"
                ref="navigationGallery"
                v-model="currentPage"
        ></b-pagination>
    <b-alert v-model="showError" dismissible variant="danger">
        {{alertMessage}}
    </b-alert>
    </div>
</template>

<script>
    export default {
        name: "photoTable",

        props: {
            photos: Array,
            profile: Object,
            userProfile: Object,
            numberOfRows: {
                default: function () {
                    return 3
                }
            },
            numberOfColumns: {
                default: function () {
                    return 6
                }
            },
            selectedImages: {
                default: function () {
                    return []
                }
            },
            adminView: {
                default: function() {
                    return false;
                }
            }
        },

        data: function () {
            return {
                currentPage: 1,
                auth: false,
                showError: false,
                alertMessage: "",
                publicDestinationPhotos: []
            }
        },

        watch: {
            profile() {
                this.checkAuth();
            }
        },

        computed: {
            rows() {
                return this.photos.length;
            },


            perPage() {
                return this.numberOfRows * this.numberOfColumns;
            },

            showDropdown() {
                return this.auth;

            }
        },

        mounted() {
            this.checkAuth()
        },

        methods: {
            /**
             * Calculates the positions of photos within a gallery grid row.
             *
             * @param rowNumber     the row currently having photos positioned within it.
             */
            getRowPhotos(rowNumber) {
                let numberOfPhotos = (this.photos.length);
                let endRowIndex = ((rowNumber * this.numberOfColumns) + ((this.currentPage - 1) * this.perPage));
                let startRowIndex = (rowNumber - 1) * this.numberOfColumns + ((this.currentPage - 1) * this.perPage);

                // Check preventing an IndexOutOfRangeError, before filling the row with photos indexed from the list.
                if (endRowIndex > numberOfPhotos) {
                    return this.photos.slice(startRowIndex);
                } else {
                    return this.photos.slice(startRowIndex, endRowIndex);
                }
            },


            /**
             * Sends a GET request to get a thumbnail image from the backend.
             */
            getThumbImage(id) {
                return 'v1/photos/thumb/' + id;
            },


            /**
             * When an image isn't shown show this default profile image.
             */
            imageAlt(event) {
                event.target.src = "../../../static/default_image.png"
            },


            /**
             * Checks the authorization of the user profile that is logged in to see if they can.
             * view the users private photos and can add or delete images from the media.
             */
            checkAuth() {
                this.auth = (this.userProfile.id === this.profile.id || this.adminView);
            },


            /**
             * Determines if a photo is selected or not.
             *
             * @param photo         the photo to be checked if selected.
             * @returns {boolean}   true if the photo is selected, false otherwise.
             */
            selected(photo) {
                for(var i = 0; i < this.selectedImages.length; i += 1) {
                    if(this.selectedImages[i].id === photo.id) {
                        return true;
                    }
                }
                return false;
            }
        }
    }
</script>

<style scoped>
    .colorBlue {
        color: white;
        font-weight: bold;
        background-color: #85BCE5;
    }

    .colorDisabled {
        background-color: #dddddd;
    }
</style>