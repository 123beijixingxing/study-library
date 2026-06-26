import { createPinia } from 'pinia'
// import piniaPersist from 'pinia-plugin-persist'
import { createPersistedState } from 'pinia-persistedstate-plugin'
// import Cookies from 'js-cookie'

const pinia = createPinia()
pinia.use(createPersistedState({ storage: window.sessionStorage }))

export default pinia
