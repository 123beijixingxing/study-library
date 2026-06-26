<template>
  <div class="hasLogoLogin_all">
    <!--顶部--start-->
    <div class="login_head" v-show="loginConfigCopy.isShowLink">
      <div class="login_logo">
        <img v-show="false" :src="base.getImgFile('/login/logoBig.png')" alt="" />
        <el-image :src="loginConfigCopy.logoImg" :preview-src-list="[loginConfigCopy.logoImg]">
          <div slot="placeholder" class="image-slot"></div>
          <div slot="error" class="image-slot">
            <!-- <i class="el-icon-picture-outline"></i> -->
          </div>
        </el-image>
      </div>
      <div class="head_content" v-if="true">
        <div class="head_language">
          <el-dropdown @command="languageClick" trigger="click">
            <div class="title_btn">
              <span class="title_link">{{ loginConfigCopy.languageType.name || pageText.language }}</span>
              <img :src="base.getImgFile('/home/down.png')" />
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="zh">{{ pageText.chinese }}</el-dropdown-item>
                <el-dropdown-item command="en">{{ pageText.english }}</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
        <div class="head_time" v-show="true">{{ loginConfigCopy.nowTime }}</div>
      </div>
    </div>
    <!--顶部--start-->
    <!--登录表单--start-->
    <div class="login_boder" :class="{ full_login_boder: !loginConfigCopy.isShowLink && !loginConfigCopy.isShowHead }">
      <div class="login_content">
        <div class="block_title">{{ loginConfigCopy.productInfo.productName || '' }}</div>
        <div class="loginLogo" v-show="false">
          <!-- <img src="@/assets/images/login/loginLogo.png" /> -->
          <!-- <img :src="loginConfigCopy.logoImg" alt="" /> -->
        </div>
        <el-form v-if="pageText.validator" :rules="rules" ref="loginForm" :model="loginConfigCopy.formLabelAlign" class="info_form">
          <el-form-item prop="username">
            <el-input :placeholder="pageText.username || '账号'" v-model="loginConfigCopy.formLabelAlign.username" @keyup.enter.native="loginConfirm">
              <template #prefix> <img slot="prefix" :src="base.getImgFile('/login/person.png')" /></template>
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              :placeholder="pageText.password || '密码'"
              type="password"
              v-model="loginConfigCopy.formLabelAlign.password"
              @keyup.enter.native="loginConfirm">
              <template #prefix> <img slot="prefix" :src="base.getImgFile('/login/pawNew.png')" /></template>
            </el-input>
          </el-form-item>
          <el-form-item class="el_form_remember">
            <el-checkbox @change="rememberAccountChoose" v-model="loginConfigCopy.isRememberAccount">{{
              pageText.rememberAccount || '记住账号'
            }}</el-checkbox>
          </el-form-item>
        </el-form>
        <div class="login_buttons">
          <el-button class="button_text" type="primary" @click="loginConfirm" size="small">{{ pageText.login || '登录' }}</el-button>
        </div>
      </div>
    </div>
    <!--登录表单--end-->
    <!--底部外链--start-->
    <div class="login_footer" v-show="loginConfigCopy.isShowLink">
      <div class="foot_item">
        <div class="item_text" v-for="(item, index) in loginConfigCopy.linkArr" :key="index">
          <a :href="item.strValue" target="_blank">{{ item.strName }}</a>
        </div>
      </div>
      <div class="link_url" v-show="false">
        <a target="_blank" href="https://beian.miit.gov.cn">
          <p>2019© 上海广拓 沪ICP备09001829号-3</p>
        </a>
      </div>
      <div class="link_url" v-show="false">
        <a target="_blank" href="http://www.beian.gov.cn/portal/registerSystemInfo?recordcode=31010602003853">
          <img class="link_icon" :src="base.getImgFile('/login/copyRight_icon.png')" />
          <p>沪公网安备 31010602003853号</p>
        </a>
      </div>
    </div>
    <!--底部外链--end-->
  </div>
</template>

<script setup lang="ts">
import { useStore } from 'vuex';
import { useRoute, useRouter } from 'vue-router';
import base from '@/utils/base';
// import type { ComponentInternalInstance } from 'vue';

const store = useStore(); // Vuex store 实例
const route = useRoute(); // 路由信息对象
const router = useRouter(); // 路由实例
const { proxy } = getCurrentInstance() as any; // proxy 替代 this
// const { appContext } = getCurrentInstance() as ComponentInternalInstance;
// const proxy = appContext.config.globalProperties;
const emit = defineEmits(['btnClick', 'loginConfirm', 'rememberAccountChoose', 'languageClick']);

const propObj = defineProps({
  tableData: {
    type: Array,
    default: () => {
      return [];
    },
  },
  loginConfig: {
    type: Object,
    default: () => {
      return {
        formLabelAlign: {
          username: '',
          password: '',
        }, // 登录信息
        isRememberAccount: true, // 是否记住账号
        // rules: {
        //   username: [{ required: true, message: '手机号或账号不能为空', trigger: 'blur' }],
        //   password: [{ required: true, message: '密码不能为空', trigger: 'blur' }]
        // }, // 登录校验
        isShowLink: true,
        linkArr: [],
        nowTime: '',
        logoImg: '',
        productInfo: { url: '', productName: '', productModel: '' },
        languageType: {
          name: '中文',
          type: 'zh',
        },
      };
    },
  },
  pageText: {
    type: Object,
    default: () => {
      return {};
    },
  },
});

// const str = ref('123'); // 声明一个 ref响应式数据

const validateUsername = (rule: any, value: any, callback: any) => {
  console.log('222', rule, value);
  if (value === '') {
    callback(new Error(proxy.$tm('tk.login.validator.tip1')));
  } else {
    callback();
  }
};
const validatePassword = (rule: any, value: any, callback: any) => {
  console.log('333', rule, value);
  if (value === '' || !value) {
    callback(new Error(proxy.$tm('tk.login.validator.tip2')));
  } else if (value && value.indexOf(' ') !== -1) {
    callback(new Error(proxy.$tm('tk.login.validator.tip3')));
  } else {
    callback();
  }
};

const data = reactive({
  loginConfigCopy: {
    formLabelAlign: {
      username: '',
      password: '',
    },
    isRememberAccount: true,
    isShowHead: true,
    isShowLink: true,
    linkArr: [],
    nowTime: '',
    logoImg: '',
    productInfo: { url: '', productName: '', productModel: '' },
    languageType: {
      name: '中文',
      type: 'zh',
    },
  } as any,
  rules: {
    // username: [{ required: true, message: '手机号或账号不能为空', trigger: 'blur' }],
    // password: [{ required: true, message: '密码不能为空', trigger: 'blur' }]
    username: [{ validator: validateUsername, trigger: 'blur' }],
    password: [{ validator: validatePassword, trigger: 'blur' }],
  },
  userInfo: store.state.userInfo,
  tableData: propObj.tableData,
  path: route.path,
  moneyTotal: 0,
  number: 0,
  flag: true as any,
}); // 声明一个 reactive响应式对象
// 使用toRefs解构
// let { } = { ...toRefs(data) }
let { loginConfigCopy, rules } = { ...toRefs(data) };

// 路由跳转
const routerPush = () => {
  router.push({ path: data.path });
};

// 登录按钮
const loginConfirm = () => {
  const object = { loginConfig: data.loginConfigCopy };
  proxy.$refs.loginForm.validate((valid: boolean) => {
    console.log('111', valid);
    valid && emit('loginConfirm', object);
    !valid && proxy.$message.error(propObj.pageText.validator.tip4);
  });
};
// 是否记住账号勾选
const rememberAccountChoose = () => {
  const object = { loginConfig: data.loginConfigCopy };
  emit('rememberAccountChoose', object);
};
// 中英文切换
const languageClick = (val: string) => {
  console.log('val', val, data.loginConfigCopy);
  // proxy.$i18n.locale = 'zh';
  // proxy.languageType = { name: val === 'zh' ? '中文' : '英文', type: 'zh' };
  proxy.$i18n.locale = val;
  const name = val === 'zh' ? proxy.$tm('tk.login.chinese') : proxy.$tm('tk.login.english');
  data.loginConfigCopy.languageType = { name: name, type: val };
  store.dispatch('setLanguageType', data.loginConfigCopy.languageType);
  proxy.$refs.loginForm.validate(() => {});
  const object = { val };
  emit('languageClick', object);
  // proxy.$refs.loginForm.resetFields();
};

onBeforeMount(() => {}); // 组件挂载之前执行
onMounted(() => {}); // 组件挂载到页面之后执行
watch(
  () => data.moneyTotal,
  val => {
    console.log('val', val);
  },
  { immediate: true, deep: true }
);
watch(
  () => propObj.loginConfig,
  val => {
    data.loginConfigCopy = JSON.parse(JSON.stringify(val));
  },
  { immediate: true, deep: true }
);
watchEffect(() => {}); // watchEffect 监听响应式数据变化
data.flag = computed(() => data.number % 2 == 0); // computed 计算属性
defineExpose({
  ...toRefs(data),
  routerPush,
  proxy,
}); // 暴露给父组件使用
</script>
<style scoped lang="less">
.hasLogoLogin_all {
  position: relative;
  width: 100%;
  height: 100%;
  // background: #222222;
  background: #ffffff;
  // 顶部
  .login_head {
    width: 100%;
    height: 17%;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0px 70px;
    box-sizing: border-box;
    .login_logo {
      width: 180px;
      height: 30px;
      img {
        display: block;
      }
      .el-image {
        width: 180px;
      }
    }
    .head_content {
      display: flex;
      align-items: center;
      justify-content: space-between;
      .head_language {
        font-family: PingFangSC-Regular;
        font-size: 14px;
        // color: #dddddd;
        color: #333333;
        .title_btn {
          display: flex;
          align-items: center;
          margin-right: 30px;
          cursor: pointer;
          // color: #dddddd !important;
          color: #333333;
          .title_link {
            margin: 0px 8px 0px 0px;
          }
        }
      }
      .head_time {
        font-size: 14px;
        // color: #dddddd;
        color: #333333;
      }
    }
  }
  // 登录表单
  .login_boder {
    width: 100%;
    height: 66%;
    background: url('../../../assets/images/png/login/bg_pic.png') no-repeat;
    background-size: 100% 100%;
    box-sizing: border-box;
    display: flex;
    align-items: center;
    padding: 0 10% 0 65%;
    :deep(.login_content) {
      position: absolute;
      width: max-content;
      // min-width: 400px;
      // min-width: calc(400 / 1920 * 100%);
      // height: calc(420 / 1080 * 100%);
      min-width: 21%;
      min-height: 420px;
      background: #ffffff;
      padding: 50px 40px 50px 40px;
      // right: 18%;
      box-sizing: border-box;
      border-radius: 3px;
      .block_title {
        font-family: PingFangSC-Medium;
        font-size: 28px;
        color: #111111;
        font-weight: 500;
        text-align: center;
        padding: 0px 0px 25px 0px;
      }
      .loginLogo {
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 20px 0px 41px 0px;
      }
      .info_form {
        .el-form-item {
          margin-bottom: 18px;
          .el-form-item__content {
            height: 42px;
            line-height: 42px;
            .el-input__wrapper {
              padding: 0px 0px 0px 0px;
            }
            .el-input__inner {
              width: 100% !important;
              height: 42px !important;
              line-height: 42px !important;
              font-family: PingFangSC-Regular;
              font-size: 14px;
              color: black !important;
              letter-spacing: 0;
              padding-left: 45px;
              background: rgba(0, 0, 0, 0.05) !important;
            }
            .el-input__inner::placeholder {
              font-size: 14px;
              color: #888888;
            }
            .el-input__prefix {
              position: absolute;
              left: 15px;
              top: 0px;
              display: flex;
              align-items: center;
            }
          }
          .el-form-item__error {
            right: 0px;
            left: auto;
          }
        }
        .el_form_remember {
          height: 30px;
          line-height: 30px;
          .el-form-item__content {
            .el-checkbox {
              padding: 0px 0px 0px 22px;
              .el-checkbox__inner {
                border: 1px solid rgba(187, 187, 187, 1);
                border-radius: 2px;
              }
              .is-checked {
                .el-checkbox__inner {
                  border-color: #26d39d;
                  border-radius: 2px;
                }
              }
              .el-checkbox__label {
                font-family: PingFangSC-Regular;
                font-size: 14px;
                color: #888888;
                font-weight: 400;
              }
            }
          }
        }
      }
      .login_buttons {
        display: flex;
        width: 100%;
        margin: 30px 0px 0px 0px;
        justify-content: space-between;
        .button_text {
          width: 100%;
          font-family: PingFangSC-Medium;
          font-size: 16px;
          color: #ffffff;
          letter-spacing: 0;
          // border-radius: 23px;
          // background: #0ec394;
          // background-color: #0ec394;
          background: #0091da;
          border-radius: 2px;
          padding: 16px 15px 16px 15px;
          span {
            font-family: PingFangSC-Medium;
            font-size: 16px;
            color: #ffffff;
            letter-spacing: 0;
            font-weight: 500;
          }
        }
      }
    }
  }
  .full_login_boder {
    height: 100%;
    .login_content {
      left: 0px;
      right: 0px;
      margin: auto;
    }
  }
  // 底部外链
  .login_footer {
    width: 100%;
    height: 17%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: flex-start;
    .foot_item {
      width: 50%;
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin: 20px 0px 18px 0px;
      .item_text {
        font-family: 'PingFangSC-Regular';
        font-size: 16px;
        color: #999999;
        cursor: pointer;
        a {
          cursor: pointer;
          color: #999999;
          text-decoration: none;
        }
      }
    }
    .link_url {
      width: 50%;
      padding: 0px 0px 10px 0px;
      a {
        width: 100%;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        color: rgb(147, 147, 147);
        cursor: pointer;
        text-decoration: none;
        .link_icon {
          padding: 0px 10px 0px 0px;
        }
        p {
          line-height: 20px;
          margin: 0;
        }
      }
    }
  }
}
</style>
