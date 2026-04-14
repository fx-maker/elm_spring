<template>
	<div class="wrapper">

		<!-- header部分 -->
		<header>
			<p>商家列表</p>
		</header>

		<!-- 商家列表部分 -->
		<ul class="business">
			<li v-for="item in businessArr" @click="toBusinessInfo(item.businessId)">
				<div class="business-img">
					<img :src="item.businessImg">
					<div class="business-img-quantity" v-show="item.quantity > 0">{{ item.quantity }}</div>
				</div>
				<div class="business-info">
					<h3>{{ item.businessName }}</h3>
					<p>&#165;{{ item.starPrice }}起送 | &#165;{{ item.deliveryPrice }}配送</p>
					<p>{{ item.businessExplain }}</p>
				</div>
			</li>
		</ul>

		<!-- 底部菜单部分 -->
		<Footer></Footer>

	</div>
</template>

<script>
	import Footer from '../components/Footer.vue';
	
	export default {
		name: 'BusinessList',
		data() {
			return {
				orderTypeId: this.$route.query.orderTypeId,
				businessArr: [],
				user: {}
			}
		},
		created() {
			this.user = this.$getSessionStorage('user');
			
			// 根据 orderTypeId 查询商家信息
			let url = `BusinessController/listBusinessByOrderTypeId/${this.orderTypeId}`;
			this.$axios.get(url).then(response => {
				// 假设后端返回格式为 CommonResult { code: 200, message: "success", result: [...] }
				if (response.data.code == 200) {
					this.businessArr = response.data.result;
					// 判断是否登录
					if (this.user !== null) {
						this.listCart();
					}
				} else {
					alert(response.data.message || '加载数据失败');
				}
			}).catch(error => {
				console.error(error);
				alert('加载数据失败，请重试');
			});
		},
		components: {
			Footer
		},
		methods: {
			listCart() {
				// 调用购物车接口获取购物车列表
				let url = `CartController/listCart/${this.user.userId}`;
				this.$axios.get(url).then(response => {
					if (response.data.code == 200) {
						let cartArr = response.data.result;
						// 遍历所有商家，计算每个商家的商品总数
						for (let businessItem of this.businessArr) {
							businessItem.quantity = 0;
							for (let cartItem of cartArr) {
								if (cartItem.businessId == businessItem.businessId) {
									businessItem.quantity += cartItem.quantity;
								}
							}
						}
						// 排序
						this.businessArr.sort();
					}
				}).catch(error => {
					console.error(error);
				});
			},
			toBusinessInfo(businessId) {
				this.$router.push({ path: '/businessInfo', query: { businessId: businessId } });
			}
		}
	}
</script>

<style scoped>
	/****************** 总容器 ******************/
	.wrapper {
		width: 100%;
		height: 100%;
	}

	/****************** header部分 ******************/
	.wrapper header {
		width: 100%;
		height: 12vw;
		background-color: #0097FF;
		color: #fff;
		font-size: 4.8vw;

		position: fixed;
		left: 0;
		top: 0;
		z-index: 1000;

		display: flex;
		justify-content: center;
		align-items: center;
	}

	/****************** 商家列表部分 ******************/
	.wrapper .business {
		width: 100%;
		margin-top: 12vw;
		margin-bottom: 14vw;
	}

	.wrapper .business li {
		width: 100%;
		box-sizing: border-box;
		padding: 2.5vw;
		border-bottom: solid 1px #DDD;
		user-select: none;
		cursor: pointer;

		display: flex;
		align-items: center;
	}

	.wrapper .business li .business-img {
		position: relative;
	}

	.wrapper .business li .business-img img {
		width: 20vw;
		height: 20vw;
	}

	.wrapper .business li .business-img .business-img-quantity {
		width: 5vw;
		height: 5vw;
		background-color: red;
		color: #fff;
		font-size: 3.6vw;
		border-radius: 2.5vw;

		display: flex;
		justify-content: center;
		align-items: center;

		position: absolute;
		right: -1.5vw;
		top: -1.5vw;
	}

	.wrapper .business li .business-info {
		margin-left: 3vw;
	}

	.wrapper .business li .business-info h3 {
		font-size: 3.8vw;
		color: #555;
	}

	.wrapper .business li .business-info p {
		font-size: 3vw;
		color: #888;
		margin-top: 2vw;
	}
</style>