📦 ItemShop

ItemShop is a lightweight CraftBukkit 1.4.6 plugin that allows players and administrators to create intuitive sign-based shops. It features automatic item name formatting, chest-linked inventories, and an infinite AdminShop mode. Compatible with bPermissions.
📌 Features

    🛒 Player Shops: Link a sign to a chest for automated trading between players.

    💎 AdminShop Mode: Create infinite server-side shops that don't require stock.

    🧹 Auto-Formatting: Automatically converts Item IDs to clean, readable names on signs.

    🛡️ Grief Protection: Only shop owners or admins can destroy shop signs.

    🎨 Visual Cleanup: Removes underscores and colons for a professional look.

🚀 Installation

    Download ItemShop.jar and place it in your server’s plugins/ folder.

    Start or restart your server.

    Use the sign format below to start creating shops!

📝 How to Create a Shop

Place a sign on or above a chest and use this format:
Line	Content	Description
1	[Shop] or [AdminShop]	Type of the shop
2	ID:Amount	Item you are selling
3	ID:Amount	Item you want as payment
4	(Empty)	Plugin will auto-fill the owner's name

Example: 264:1 on line 2 and 266:5 on line 3 will sell 1 Diamond for 5 Gold Ingots.
🔑 Permissions

    itemshop.use – Allows players to trade using signs (Default: everyone).

    itemshop.create – Allows players to create their own shops (Default: op).

    itemshop.admin – Allows creating AdminShops and destroying any shop (Default: op).

📄 License

This project is licensed under the GNU General Public License v3.0 (GPL-3.0). See LICENSE for details.
