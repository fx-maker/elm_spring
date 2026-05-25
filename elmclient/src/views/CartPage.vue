<template>
	<div class="wrapper">
		<header>
			<p>购物车</p>
		</header>

		<div class="cart-empty" v-if="groupedCart.length === 0">
			<div class="empty-icon">
				<i class="fa fa-shopping-cart"></i>
			</div>
			<p>购物车是空的</p>
			<div class="empty-btn" @click="$router.push('/index')">去选购</div>
		</div>

		<div class="cart-content" v-else>
			<div class="business-group" v-for="group in groupedCart" :key="group.businessId">
				<div class="group-header">
					<div class="group-business" @click="goBusiness(group.businessId)">
						<img :src="group.business.businessImg">
						<div class="business-info">
							<p class="business-name">{{group.business.businessName}}</p>
							<p class="business-delivery">配送费 {{group.business.deliveryPrice}}元</p>
						</div>
						<i class="fa fa-angle-right"></i>
					</div>
				</div>
				<ul class="food-list">
					<li v-for="item in group.items" :key="item.cartId">
						<div class="food-left">
							<img :src="item.food.foodImg">
							<div class="food-info">
								<p class="food-name">{{item.food.foodName}}</p>
								<p class="food-price">&#165;{{item.food.foodPrice}}</p>
							</div>
						</div>
						<div class="food-right">
							<div class="quantity-control">
								<i class="fa fa-minus-circle" @click="minus(item)"></i>
								<span>{{item.quantity}}</span>
								<i class="fa fa-plus-circle" @click="add(item)"></i>
							</div>
							<p class="subtotal">&#165;{{(item.food.foodPrice * item.quantity).toFixed(2)}}</p>
						</div>
					</li>
				</ul>
				<div class="group-footer">
					<div class="clear-business" @click="clearBusiness(group)">
						清空该商家
					</div>
					<div class="group-total">
						小计：&#165;{{group.businessTotal.toFixed(2)}}
					</div>
				</div>
			</div>
		</div>

		<div class="cart-footer" v-if="groupedCart.length > 0">
			<div class="footer-left">
				<div class="total-info">
					<p class="total-label">合计</p>
					<p class="total-amount">&#165;{{totalPrice.toFixed(2)}}</p>
				</div>
			</div>
			<div class="footer-right" @click="goOrder">
				去结算
			</div>
		</div>

		<Footer class="page-footer" />
	</div>
</template>

<script>
import Footer from '@/components/Footer.vue';

export default {
	name: 'CartPage',
	components: { Footer },
	data() {
		return {
			cartArr: [],
			user: {}
		};
	},
	created() {
		const user = this.$getSessionStorage('user');
		if (!user) {
			this.$router.push('/login');
			return;
		}
		this.user = user;
		this.loadCart();
	},
	computed: {
		groupedCart() {
			const map = {};
			for (let item of this.cartArr) {
				const bid = item.businessId;
				if (!map[bid]) {
					map[bid] = {
						businessId: bid,
						business: item.business,
						items: []
					};
				}
				map[bid].items.push(item);
			}
			for (let key in map) {
				map[key].businessTotal = map[key].items.reduce(
					(sum, item) => sum + item.food.foodPrice * item.quantity, 0
				);
			}
			return Object.values(map);
		},
		totalPrice() {
			return this.groupedCart.reduce((sum, group) => sum + group.businessTotal, 0);
		}
	},
	methods: {
		loadCart() {
			this.$axios.get(`CartController/listCart/${this.user.userId}`).then(response => {
				if (response.data.code === 200) {
					this.cartArr = response.data.result || [];
				}
			}).catch(error => {
				console.error(error);
			});
		},
		add(item) {
			let url = `CartController/updateCart/${this.user.userId}/${item.businessId}/${item.foodId}/${item.quantity + 1}`;
			this.$axios.put(url).then(response => {
				if (response.data.code === 200) {
					item.quantity += 1;
				}
			}).catch(error => {
				console.error(error);
			});
		},
		minus(item) {
			if (item.quantity > 1) {
				let url = `CartController/updateCart/${this.user.userId}/${item.businessId}/${item.foodId}/${item.quantity - 1}`;
				this.$axios.put(url).then(response => {
					if (response.data.code === 200) {
						item.quantity -= 1;
					}
				}).catch(error => {
					console.error(error);
				});
			} else {
				this.removeItem(item);
			}
		},
		removeItem(item) {
			let url = `CartController/removeCart/${this.user.userId}/${item.businessId}/${item.foodId}`;
			this.$axios.delete(url).then(response => {
				if (response.data.code === 200) {
					this.cartArr = this.cartArr.filter(c => c.cartId !== item.cartId);
				}
			}).catch(error => {
				console.error(error);
			});
		},
		clearBusiness(group) {
			let url = `CartController/removeCart/${this.user.userId}/${group.businessId}`;
			this.$axios.delete(url).then(response => {
				if (response.data.code === 200) {
					this.cartArr = this.cartArr.filter(c => c.businessId !== group.businessId);
				}
			}).catch(error => {
				console.error(error);
			});
		},
		goBusiness(businessId) {
			this.$router.push({ path: '/businessInfo', query: { businessId } });
		},
		goOrder() {
			if (this.groupedCart.length === 1) {
				this.$router.push({ path: '/orders', query: { businessId: this.groupedCart[0].businessId } });
			} else {
				alert('请先选择一个商家结算');
			}
		}
	}
};
</script>

<style scoped>
.wrapper {
	width: 100%;
	min-height: 100vh;
	background-color: #f5f7fa;
	padding-top: 12vw;
	padding-bottom: 18vw;
}

.wrapper header {
	width: 100%;
	height: 12vw;
	background-color: #0097FF;
	color: #fff;
	font-size: 4.8vw;
	font-weight: 500;
	position: fixed;
	top: 0;
	left: 0;
	z-index: 1000;
	display: flex;
	justify-content: center;
	align-items: center;
}

.cart-empty {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding-top: 30vw;
}

.cart-empty .empty-icon {
	font-size: 20vw;
	color: #ccc;
}

.cart-empty p {
	font-size: 4vw;
	color: #999;
	margin: 3vw 0;
}

.cart-empty .empty-btn {
	padding: 2vw 6vw;
	background: #0097FF;
	color: #fff;
	border-radius: 4vw;
	font-size: 3.5vw;
}

.cart-content {
	padding: 2vw;
}

.business-group {
	background: #fff;
	border-radius: 3vw;
	margin-bottom: 3vw;
	overflow: hidden;
}

.group-header {
	padding: 3vw;
	border-bottom: 1px solid #f0f0f0;
}

.group-business {
	display: flex;
	align-items: center;
	gap: 3vw;
	cursor: pointer;
}

.group-business img {
	width: 10vw;
	height: 10vw;
	border-radius: 2vw;
}

.group-business .business-info {
	flex: 1;
}

.group-business .business-name {
	font-size: 4vw;
	font-weight: 700;
	color: #333;
}

.group-business .business-delivery {
	font-size: 3vw;
	color: #999;
	margin-top: 1vw;
}

.group-business .fa-angle-right {
	font-size: 5vw;
	color: #999;
}

.food-list li {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 3vw;
	border-bottom: 1px solid #f5f5f5;
}

.food-left {
	display: flex;
	align-items: center;
	gap: 3vw;
}

.food-left img {
	width: 14vw;
	height: 14vw;
	border-radius: 2vw;
}

.food-left .food-info .food-name {
	font-size: 3.8vw;
	color: #333;
}

.food-left .food-info .food-price {
	font-size: 3.5vw;
	color: #f90;
	margin-top: 1vw;
}

.food-right {
	display: flex;
	flex-direction: column;
	align-items: flex-end;
	gap: 1vw;
}

.quantity-control {
	display: flex;
	align-items: center;
	gap: 2vw;
}

.quantity-control .fa-minus-circle {
	font-size: 5vw;
	color: #999;
	cursor: pointer;
}

.quantity-control .fa-plus-circle {
	font-size: 5vw;
	color: #0097EF;
	cursor: pointer;
}

.quantity-control span {
	font-size: 4vw;
	color: #333;
	min-width: 5vw;
	text-align: center;
}

.subtotal {
	font-size: 3.8vw;
	color: #333;
	font-weight: 700;
}

.group-footer {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 2vw 3vw;
	background: #fafafa;
}

.clear-business {
	font-size: 3vw;
	color: #999;
}

.group-total {
	font-size: 3.8vw;
	color: #333;
	font-weight: 700;
}

.cart-footer {
	position: fixed;
	bottom: 14vw;
	left: 0;
	width: 100%;
	height: 13vw;
	background: #fff;
	border-top: 1px solid #eee;
	display: flex;
	z-index: 900;
}

.footer-left {
	flex: 1;
	display: flex;
	align-items: center;
	padding-left: 4vw;
}

.total-info .total-label {
	font-size: 3vw;
	color: #999;
}

.total-info .total-amount {
	font-size: 5vw;
	font-weight: 700;
	color: #f90;
}

.footer-right {
	width: 28vw;
	background: #38CA73;
	color: #fff;
	font-size: 4.5vw;
	font-weight: 700;
	display: flex;
	justify-content: center;
	align-items: center;
	cursor: pointer;
}

.page-footer {
	position: fixed;
	bottom: 0;
	left: 0;
	width: 100%;
	z-index: 999;
}
</style>
